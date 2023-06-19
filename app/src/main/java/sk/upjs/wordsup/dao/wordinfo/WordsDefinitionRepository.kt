package sk.upjs.wordsup.dao.wordinfo

import sk.upjs.wordsup.rest.Phonetic
import androidx.annotation.WorkerThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import sk.upjs.wordsup.dao.word.Word
import sk.upjs.wordsup.rest.RestApi
import javax.inject.Inject

class WordsDefinitionRepository @Inject constructor() {

    @WorkerThread
    suspend fun getWordsInfos(list: List<Word>): StateFlow<MutableList<WordInfo>> {
        val words = MutableStateFlow<MutableList<WordInfo>>(mutableListOf())

        withContext(Dispatchers.Default) {
            val deferredResults = list.map { word ->
                async {
                    var definitions = mutableListOf<String>()
                    var phonetic = Phonetic()

                    try {
                        val response = RestApi.wordsRestDao.getDictionaryData(word.word)
                        response.forEach { responseModel ->
                            responseModel.meanings?.forEach { meaning ->
                                meaning.definitions?.forEach {
                                    definitions.add(it.definition.toString())
                                }
                            }
                            responseModel.phonetics?.forEach {
                                if (it.audio?.contains("uk") == true) {
                                    phonetic = it
                                }
                            }
                        }
                    } catch (e: Exception) {
                        definitions = mutableListOf("Sorry, we didn't find a definition for this word")
                        phonetic = Phonetic("","")
                    }
                    WordInfo(definitions, phonetic)
                }
            }
            val wordInfos = deferredResults.awaitAll()
            words.value = wordInfos.toMutableList()
        }

        return words.asStateFlow()
    }
}

package sk.upjs.wordsup.dao.WordsByQuiz

import Phonetic
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import sk.upjs.wordsup.dao.word.Word
import sk.upjs.wordsup.dao.wordinfo.WordInfo
import sk.upjs.wordsup.rest.RestApi
import javax.inject.Inject

class WordsDefinitionRepository @Inject constructor() {

    @WorkerThread
    suspend fun getWordsInfos(list: List<Word>): StateFlow<MutableList<WordInfo>> {
        val words = MutableStateFlow<MutableList<WordInfo>>(mutableListOf())

        withContext(Dispatchers.Default) {
            val deferredResults = list.map { word ->
                async {
                    val definitions = mutableListOf<String>()
                    var phonetic = Phonetic()

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
                    WordInfo(definitions, phonetic)
                }
            }
            val wordInfos = deferredResults.awaitAll()
            words.value = wordInfos.toMutableList()
        }

        return words.asStateFlow()
    }
}

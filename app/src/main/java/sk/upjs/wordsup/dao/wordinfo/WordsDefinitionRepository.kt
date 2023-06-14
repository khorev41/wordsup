package sk.upjs.wordsup.dao.WordsByQuiz


import Phonetic
import androidx.annotation.WorkerThread
import sk.upjs.wordsup.dao.word.Word
import sk.upjs.wordsup.dao.word.WordsDao
import sk.upjs.wordsup.dao.wordinfo.WordInfo
import sk.upjs.wordsup.rest.RestApi
import javax.inject.Inject

class WordsDefinitionRepository @Inject constructor(private val dao: WordsDao) {

    @WorkerThread
    suspend fun getWordsInfos(list: List<Word>): MutableMap<String, WordInfo> {

        var map = mutableMapOf<String, WordInfo>()

        for (word in list){
            var definitions = mutableListOf<String>()
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

            map.put(word.word, WordInfo(definitions, phonetic))
        }
        return map
    }

    @WorkerThread
    suspend fun getWordInfo(word: Word) : WordInfo{
        var definitions = mutableListOf<String>()
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
        return WordInfo(definitions, phonetic)
    }
}
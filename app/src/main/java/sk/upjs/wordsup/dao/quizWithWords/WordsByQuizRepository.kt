package sk.upjs.wordsup.dao.quizWithWords



import Phonetic
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import sk.upjs.wordsup.dao.Quiz
import sk.upjs.wordsup.dao.Word
import sk.upjs.wordsup.dao.WordsDao
import sk.upjs.wordsup.dao.wordinfo.WordInfo
import sk.upjs.wordsup.rest.RestApi
import javax.inject.Inject

class WordsByQuizRepository @Inject constructor(private val dao: WordsDao) {

    fun getWordsByQuiz(quiz: Quiz): Flow<List<Word>> {
        return dao.getWordsByQuizId(quiz.quizId)
    }

    suspend fun getWordsInfos(list: List<Word>): Map<String, WordInfo> {

        var map = emptyMap<String, WordInfo>()
        CoroutineScope(Dispatchers.IO).launch {
            list.forEach {
                var phonetic = Phonetic()
                var definitions = mutableListOf<String>()
                try {
                    val response = RestApi.wordsRestDao.getDictionaryData(it.word)
                    response.forEach {
                        it.meanings?.forEach {
                            it.definitions?.forEach{
                                definitions.add(it.definition.toString())
                            }
                        }
                        it.phonetics?.forEach {
                            if(phonetic.audio?.contains("uk") == true){
                                phonetic = it
                            }
                        }
                    }

                    map.plus(Pair(it.word, WordInfo(it.word, definitions,phonetic)))

                } catch (e: Exception) {
                    // Handle the error
                }
            }
        }
        map.plus(Pair("S", WordInfo("asda", listOf("defs"),Phonetic("link","pronouns"))))
        return map
    }


    suspend fun insertWords(list: List<Word>) {
        try {
            dao.insert(list)
        } catch (e: Exception) {
            Log.e("QUIZREPOSITORY", e.toString())
        }
    }
}
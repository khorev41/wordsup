package sk.upjs.wordsup.dao.word

import sk.upjs.wordsup.dao.WordsDao
import javax.inject.Inject

class WordRepository @Inject constructor(private val dao: WordsDao) {

    val words = dao.getWords()

//    @WorkerThread
//    suspend fun insertQuizzes(list :List<Quiz> ) {
//        try {
//            dao.insert(list)
//        } catch (e: Exception) {
//            Log.e("QUIZREPOSITORY", e.toString())
//        }
//    }

}
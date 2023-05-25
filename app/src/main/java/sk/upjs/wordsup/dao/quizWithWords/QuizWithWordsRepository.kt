package sk.upjs.wordsup.dao.quizWithWords


import android.util.Log
import sk.upjs.wordsup.dao.Quiz
import sk.upjs.wordsup.dao.QuizDao
import javax.inject.Inject

class QuizWithWordsRepository @Inject constructor(private val dao: QuizDao) {

    val quizzesWithWords = dao.getQuizWithWords()

    suspend fun insertQuizzes(list :List<Quiz> ) {
        try {
            dao.insert(list)
        } catch (e: Exception) {
            Log.e("QUIZREPOSITORY", e.toString())
        }
    }
}
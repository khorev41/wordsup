package sk.upjs.wordsup.dao.quiz

import android.util.Log
import androidx.annotation.WorkerThread
import sk.upjs.wordsup.dao.Quiz
import sk.upjs.wordsup.dao.QuizDao
import javax.inject.Inject

class QuizRepository @Inject constructor(private val dao: QuizDao) {

    val quizzes = dao.getQuizzes()


    suspend fun insertQuizzes(list :List<Quiz> ) {
        try {
            dao.insert(list)
        } catch (e: Exception) {
            Log.e("QUIZREPOSITORY", e.toString())
        }
    }

}
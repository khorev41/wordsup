package sk.upjs.wordsup.dao.quiz

import android.util.Log
import javax.inject.Inject

class QuizRepository @Inject constructor(private val dao: QuizDao) {

    val quizzes = dao.getQuizzesWithWords()


    suspend fun insertQuizzes(list :List<Quiz> ) {
        try {
            dao.insert(list)
        } catch (e: Exception) {
            Log.e("QUIZREPOSITORY", e.toString())
        }
    }

}
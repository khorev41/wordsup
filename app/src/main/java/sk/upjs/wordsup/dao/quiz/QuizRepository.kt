package sk.upjs.wordsup.dao.quiz

import android.util.Log
import androidx.annotation.WorkerThread
import sk.upjs.wordsup.dao.tries.Try
import javax.inject.Inject

class QuizRepository @Inject constructor(private val dao: QuizDao) {


    val quizzes = dao.getQuizzesWithWords()

    @WorkerThread
    suspend fun insertQuizWithWords(quiz: QuizWithWords) {
        try {
            dao.insertQuizWithWords(quiz)
        } catch (e: Exception) {
            Log.e("QUIZREPOSITORY", e.toString())
        }

    }

    @WorkerThread
    suspend fun deleteQuizWithWords(quiz: QuizWithWords) {
        try {
            dao.deleteQuizWithWords(quiz)
        } catch (e: Exception) {
            Log.e("QUIZ REPOSITORY", e.toString())
        }
    }


    @WorkerThread
    suspend fun insertQuizzes(list: List<Quiz>) {
        try {
            dao.insertQuizzes(list)
        } catch (e: Exception) {
            Log.e("QUIZREPOSITORY", e.toString())
        }
    }

    @WorkerThread
    suspend fun insertTries(list: List<Try>) {
        try {
            dao.insertTries(list)
        } catch (e: Exception) {
            Log.e("QUIZREPOSITORY", e.toString())
        }
    }

}
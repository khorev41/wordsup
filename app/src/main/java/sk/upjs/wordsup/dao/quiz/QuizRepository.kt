package sk.upjs.wordsup.dao.quiz

import android.util.Log
import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuizRepository @Inject constructor(private val dao: QuizDao) {

    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default

    val quizzes = dao.getQuizzesWithWords()

    @WorkerThread
    suspend fun insertQuizWithWords(quiz: QuizWithWords ){
        try {
            dao.insertQuizWithWords(quiz)
        } catch (e: Exception) {
            Log.e("QUIZREPOSITORY", e.toString())
        }
    }

    @WorkerThread
    suspend fun insertQuizzes(list :List<Quiz> ) {
        try {
            dao.insert(list)
        } catch (e: Exception) {
            Log.e("QUIZREPOSITORY", e.toString())
        }
    }

}
package sk.upjs.wordsup.dao.word

import android.util.Log
import androidx.annotation.WorkerThread
import sk.upjs.wordsup.dao.quiz.QuizWithWords
import sk.upjs.wordsup.dao.quiz.QuizWordCrossRef
import javax.inject.Inject

class WordRepository @Inject constructor(private val dao: WordsDao) {

    val words = dao.getWords()

    @WorkerThread
    suspend fun deleteQuizWordCrossRef(quizId: Long, words: List<Long>){
        try {
            dao.deleteQuizWordCrossRef(quizId, words)
        } catch (e: Exception) {
            Log.e("QUIZREPOSITORY", e.toString())
        }
    }

}
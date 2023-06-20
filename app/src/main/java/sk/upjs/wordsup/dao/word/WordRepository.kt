package sk.upjs.wordsup.dao.word

import android.util.Log
import androidx.annotation.WorkerThread
import javax.inject.Inject

class WordRepository @Inject constructor(private val dao: WordsDao) {

    val words = dao.getWords()

    @WorkerThread
    suspend fun deleteQuizWordCrossRef(quizId: Long, word: Long) {
        try {
            dao.deleteQuizWordCrossRef(quizId, word)

            if (dao.getQuizWordCrossRefByWordId(word).isEmpty()) {
                dao.deleteWordById(word)
            }

        } catch (e: Exception) {
            Log.e("WORD-REPOSITORY", e.toString())
        }
    }

}
package sk.upjs.wordsup.dao.word

import android.util.Log
import androidx.annotation.WorkerThread
import javax.inject.Inject

class WordRepository @Inject constructor(private val dao: WordsDao) {

    val words = dao.getWords()

    @WorkerThread
    suspend fun deleteQuizWordCrossRef(quizId: Long, words: List<Long>){
        try {
            dao.deleteQuizWordCrossRef(quizId, words)
            words.forEach {
                if (dao.getQuizWordCrossRefByWordId(it).isNullOrEmpty()) {
                    dao.deleteWordById(it)
                }
            }

        } catch (e: Exception) {
            Log.e("QUIZREPOSITORY", e.toString())
        }
    }

}
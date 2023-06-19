package sk.upjs.wordsup.dao.tries

import android.util.Log
import androidx.annotation.WorkerThread
import javax.inject.Inject

class TryRepository @Inject constructor(private val dao: TryDao) {

    val tries = dao.getTriesWithQuiz()

    @WorkerThread
    suspend fun deleteTries(tries : List<Try>){
        try {
            dao.delete(tries)

        } catch (e: Exception) {
            Log.e("TRY REPOSITORY", e.toString())
        }
    }

    @WorkerThread
    suspend fun insert(tries : List<Try>){
        try {
            dao.insert(tries)
        } catch (e: Exception) {
            Log.e("TRY REPOSITORY", e.toString())
        }
    }

}
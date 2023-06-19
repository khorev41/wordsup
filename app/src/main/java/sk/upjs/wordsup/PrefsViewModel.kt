package sk.upjs.wordsup.dao.quiz

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrefsViewModel @Inject constructor(private var repository: PrefRepository) : ViewModel() {

    fun clearDatabase(){
        viewModelScope.launch {
            repository.clearDatabase()
        }
    }


}
class PrefRepository @Inject constructor(private val dao: QuizDao) {
    @WorkerThread
    suspend fun clearDatabase() {
        try {
            dao.clearDatabase()
        } catch (e: Exception) {
            Log.e("PREF REPOSITORY", e.toString())
        }

    }
}






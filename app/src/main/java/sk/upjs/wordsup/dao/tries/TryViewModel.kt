package sk.upjs.wordsup.dao.tries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TryViewModel @Inject constructor(private val repository: TryRepository) : ViewModel() {

    val allTries = repository.tries.asLiveData()


    fun insert(tries : List<Try>){
        viewModelScope.launch {
            repository.insert(tries)
        }
    }


}
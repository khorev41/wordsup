package sk.upjs.wordsup.dao.word

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WordViewModel @Inject constructor(private val repository: WordRepository) : ViewModel() {

    val allWords = repository.words.asLiveData()

    class WordViewModelFactory(private val repository: WordRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
                return WordViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }

}
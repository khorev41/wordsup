package sk.upjs.wordsup.dao.wordinfo

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import sk.upjs.wordsup.dao.word.Word
import sk.upjs.wordsup.dao.WordsByQuiz.WordsDefinitionRepository
import javax.inject.Inject

@HiltViewModel
class WordsDefinitionViewModel @Inject constructor(private val repository: WordsDefinitionRepository) : ViewModel() {

    private val _wordsInfos = MutableLiveData<MutableList<WordInfo>>()
    val wordsInfos: LiveData<MutableList<WordInfo>> = _wordsInfos

    fun getWordsInfos(list: List<Word>) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getWordsInfos(list)
            _wordsInfos.postValue(result.value)
        }
    }

    class WordsByQuizViewModelFactory(private val repository: WordsDefinitionRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WordsDefinitionViewModel::class.java)) {
                return WordsDefinitionViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }



}


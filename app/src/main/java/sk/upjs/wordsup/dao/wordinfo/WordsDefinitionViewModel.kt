package sk.upjs.wordsup.dao.wordinfo

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sk.upjs.wordsup.dao.word.Word
import sk.upjs.wordsup.dao.WordsByQuiz.WordsDefinitionRepository
import javax.inject.Inject

@HiltViewModel
class WordsDefinitionViewModel @Inject constructor(private val repository: WordsDefinitionRepository) : ViewModel() {

     fun getWordsInfos(list: List<Word>): LiveData<MutableMap<String, WordInfo>> {
        val result = MutableLiveData<MutableMap<String, WordInfo>>()
        viewModelScope.launch {
            val res = repository.getWordsInfos(list)
            result.postValue(res)
        }
        return result
    }

    fun getWordInfo(word: Word): LiveData<WordInfo>{
        val result = MutableLiveData<WordInfo>()
        viewModelScope.launch {
            val res = repository.getWordInfo(word)
            result.postValue(res)
        }
        return result
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


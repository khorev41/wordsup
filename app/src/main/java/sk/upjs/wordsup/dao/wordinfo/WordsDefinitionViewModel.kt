package sk.upjs.wordsup.dao.wordinfo

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sk.upjs.wordsup.dao.word.Word
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


}


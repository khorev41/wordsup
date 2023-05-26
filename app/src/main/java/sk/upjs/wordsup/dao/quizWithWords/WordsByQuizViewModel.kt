package sk.upjs.wordsup.dao.quizWithWords

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import sk.upjs.wordsup.dao.Quiz
import sk.upjs.wordsup.dao.Word
import sk.upjs.wordsup.dao.wordinfo.WordInfo
import javax.inject.Inject

@HiltViewModel
class WordsByQuizViewModel @Inject constructor(private val repository: WordsByQuizRepository) : ViewModel() {


    fun getWordsByQuiz(quiz: Quiz): LiveData<List<Word>> {
        return repository.getWordsByQuiz(quiz = quiz).asLiveData()
    }

    suspend fun getWordsInfos(list: List<Word>): Map<String, WordInfo>{
        return repository.getWordsInfos(list)
    }



    class WordsByQuizViewModelFactory(private val repository: WordsByQuizRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WordsByQuizViewModel::class.java)) {
                return WordsByQuizViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }



}


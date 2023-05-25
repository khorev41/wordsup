package sk.upjs.wordsup.dao.quiz

import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sk.upjs.wordsup.dao.Quiz
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(private val repository: QuizRepository) : ViewModel() {

    val allQuizzes = repository.quizzes.asLiveData()

    fun insertQuiz(list: List<Quiz>) {
        viewModelScope.launch {
            repository.insertQuizzes(list)
        }
    }

    class QuizViewModelFactory(private val repository: QuizRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
                return QuizViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }



}






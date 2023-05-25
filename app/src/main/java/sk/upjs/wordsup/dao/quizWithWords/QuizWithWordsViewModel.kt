package sk.upjs.wordsup.dao.quizWithWords

import sk.upjs.wordsup.dao.quiz.QuizRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sk.upjs.wordsup.dao.Quiz
import javax.inject.Inject

@HiltViewModel
class QuizWithWordsViewModel @Inject constructor(private val repository: QuizWithWordsRepository) : ViewModel() {

    val allQuizzesWithWords = repository.quizzesWithWords.asLiveData()

    fun insertQuiz(list: List<Quiz>) {
        viewModelScope.launch {
            repository.insertQuizzes(list)
        }
    }

    class QuizWithWordsViewModelFactory(private val repository: QuizWithWordsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(QuizWithWordsViewModel::class.java)) {
                return QuizWithWordsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }



}


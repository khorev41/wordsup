package sk.upjs.wordsup.dao.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sk.upjs.wordsup.dao.word.Word
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(private var repository: QuizRepository) : ViewModel() {

    val allQuizzes = repository.quizzes.asLiveData()
    fun insertQuiz(list: List<Quiz>) {
        viewModelScope.launch {
            repository.insertQuizzes(list)
        }
    }


    fun insertQuizWithWords(quiz: QuizWithWords) {
        viewModelScope.launch {
            repository.insertQuizWithWords(quiz)
        }
    }


}






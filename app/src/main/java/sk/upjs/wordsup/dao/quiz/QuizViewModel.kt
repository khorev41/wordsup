package sk.upjs.wordsup.dao.quiz

import androidx.lifecycle.MutableLiveData
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
    val quizSavedId: MutableLiveData<Long> = MutableLiveData(0)

    fun insertQuizWithWord(quiz: QuizWithWords) {
        viewModelScope.launch {
           repository.insertQuizWithWords(quiz)
        }
    }

    fun insertQuizAndWord(quiz: Quiz, word: Word) {
        viewModelScope.launch {
            quizSavedId.value = repository.insertQuizAndWord(quiz, word)
        }
    }

    fun deleteQuiz(quiz: QuizWithWords) {
        viewModelScope.launch {
            repository.deleteQuizWithWords(quiz)
        }
    }

    fun insertQuizz(quiz: Quiz) {
        viewModelScope.launch {
            repository.insertQuizzes(quiz)
        }
    }


}






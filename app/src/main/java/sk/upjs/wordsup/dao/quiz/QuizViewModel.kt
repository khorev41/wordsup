package sk.upjs.wordsup.dao.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(repository: QuizRepository) : ViewModel() {

    val allQuizzes = repository.quizzes.asLiveData()

//    fun insertQuiz(list: List<QuizWithWords>) {
//        viewModelScope.launch {
//            repository.insertQuizzes(list)
//        }
//    }


}






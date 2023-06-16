package sk.upjs.wordsup.dao.word

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sk.upjs.wordsup.dao.quiz.Quiz
import sk.upjs.wordsup.dao.quiz.QuizWithWords
import sk.upjs.wordsup.dao.quiz.QuizWordCrossRef
import javax.inject.Inject

@HiltViewModel
class WordViewModel @Inject constructor(private val repository: WordRepository) : ViewModel() {

    val allWords = repository.words.asLiveData()

    fun deleteWordsFromQuiz(quiz: QuizWithWords, words: List<Word>){
        val wordsIds = words.map { it.wordId }
        viewModelScope.launch {
            repository.deleteQuizWordCrossRef(quiz.quiz.quizId, wordsIds)
        }
    }


}
package sk.upjs.wordsup

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import sk.upjs.wordsup.dao.Quiz
import sk.upjs.wordsup.dao.QuizDao
import sk.upjs.wordsup.dao.Word
import sk.upjs.wordsup.dao.quiz.QuizRepository
import sk.upjs.wordsup.dao.quizWithWords.QuizWithWordsRepository
import sk.upjs.wordsup.dao.quizWithWords.QuizWithWordsViewModel
import sk.upjs.wordsup.rest.RestApi
@AndroidEntryPoint
class QuizActivity : AppCompatActivity() {

    private val viewModel: QuizWithWordsViewModel by viewModels<QuizWithWordsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val quiz = intent.getSerializableExtra("quiz") as Quiz

        viewModel.allQuizzesWithWords.observe(this, Observer {
            findViewById<TextView>(R.id.word_definition_textview).setText(it?.toMutableList()?.get(0)?.words.toString())
        })
    }

}
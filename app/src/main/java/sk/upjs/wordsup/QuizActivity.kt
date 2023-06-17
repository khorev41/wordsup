package sk.upjs.wordsup

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator
import dagger.hilt.android.AndroidEntryPoint
import sk.upjs.wordsup.dao.quiz.QuizWithWords
import sk.upjs.wordsup.dao.wordinfo.WordInfo
import sk.upjs.wordsup.dao.wordinfo.WordsDefinitionViewModel
import sk.upjs.wordsup.fragments.QuizPlayFragment

@AndroidEntryPoint
class QuizActivity : AppCompatActivity() {

    private val viewModel: WordsDefinitionViewModel by viewModels()

    private lateinit var quiz: QuizWithWords
    private var wordInfo = mutableListOf<WordInfo>()
    private var wasStartedQuiz = false;

    fun openFragment(fragment: Fragment?) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment!!)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)


        quiz = intent.getSerializableExtra("quiz") as QuizWithWords

        quiz.words = quiz.words.shuffled()


        findViewById<TextView>(R.id.quiz_name).text = quiz.quiz.name
        findViewById<TextView>(R.id.words_number).text = quiz.words.size.toString() + " WORDS"

        findViewById<MaterialButton>(R.id.start_quiz).setOnClickListener {
            openFragment(QuizPlayFragment.newInstance(wordInfo, quiz))
            wasStartedQuiz =true
        }

        viewModel.getWordsInfos(quiz.words)
        viewModel.wordsInfos.observe(this) { list ->
            wordInfo = list
            findViewById<MaterialButton>(R.id.start_quiz).isEnabled = true
            findViewById<MaterialButton>(R.id.start_quiz).text =resources.getString(R.string.start_quiz)
            findViewById<ProgressBar>(R.id.loading_circle).visibility = View.GONE;
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("quiz", quiz)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        quiz = savedInstanceState.getSerializable("quiz") as QuizWithWords
    }

    override fun onBackPressed() {
        if(wasStartedQuiz) {
            MaterialAlertDialogBuilder(this).setTitle(getString(R.string.are_you_sure))
                .setMessage(getString(R.string.changes_will_not_be_saved))
                .setNegativeButton(getString(R.string.no)) { dialog, which ->
                    // Respond to negative button press
                }.setPositiveButton(getString(R.string.yes)) { dialog, which ->
                    finish()
                }.show()
        }else{
            finish()
        }
    }


}
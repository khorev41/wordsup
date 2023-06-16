package sk.upjs.wordsup

import android.app.AlertDialog
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import sk.upjs.wordsup.dao.quiz.QuizWithWords
import sk.upjs.wordsup.dao.wordinfo.WordInfo
import sk.upjs.wordsup.dao.wordinfo.WordsDefinitionViewModel
import sk.upjs.wordsup.fragments.QuizPlayFragment

@Suppress("DEPRECATION")
@AndroidEntryPoint
class QuizActivity : AppCompatActivity() {

    private val viewModel: WordsDefinitionViewModel by viewModels()

    private lateinit var quiz: QuizWithWords
    private var wordInfo = mutableListOf<WordInfo>()

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


        findViewById<TextView>(R.id.quiz_name).text = quiz.quiz.name
        findViewById<TextView>(R.id.words_number).text = quiz.words.size.toString() + " WORDS"

        findViewById<MaterialButton>(R.id.start_quiz).setOnClickListener {
            openFragment(QuizPlayFragment.newInstance(wordInfo, quiz))
        }

        viewModel.getWordsInfos(quiz.words)
        viewModel.wordsInfos.observe(this) { list ->
            wordInfo = list
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
        val alertDialog =
            AlertDialog.Builder(this).setTitle("Are you sure?").setMessage("Do you want to exit?")
                .setPositiveButton("Yes") { _, _ ->
                    super.onBackPressed()
                    super.onBackPressed()
                }.setNegativeButton("No") { _, _ ->
                    // Do nothing
                }.create()
        alertDialog.show()
    }


}
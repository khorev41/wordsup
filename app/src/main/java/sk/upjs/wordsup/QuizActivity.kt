package sk.upjs.wordsup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import sk.upjs.wordsup.dao.quiz.QuizWithWords
import sk.upjs.wordsup.fragments.FinishQuizFragment
import sk.upjs.wordsup.fragments.LearnFragment
import sk.upjs.wordsup.fragments.QuizPlayFragment
import sk.upjs.wordsup.fragments.StartQuizFragment

@AndroidEntryPoint
class QuizActivity : AppCompatActivity() {

    private lateinit var quiz: QuizWithWords
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

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("quiz", quiz)
        outState.putBoolean("wasStartedQuiz", wasStartedQuiz)
    }

    override fun onResume() {
        super.onResume()
        if (!wasStartedQuiz) {
            openFragment(StartQuizFragment.newInstance(quiz))
            wasStartedQuiz = true
        }

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        quiz = savedInstanceState.getSerializable("quiz") as QuizWithWords
        wasStartedQuiz = savedInstanceState.getBoolean("wasStartedQuiz")

    }

    override fun onBackPressed() {
        when (supportFragmentManager.fragments.last()) {
            is QuizPlayFragment -> {
                MaterialAlertDialogBuilder(this).setTitle(getString(R.string.are_you_sure))
                    .setMessage(getString(R.string.changes_will_not_be_saved))
                    .setNegativeButton(getString(R.string.no)) { dialog, which ->
                    }.setPositiveButton(getString(R.string.yes)) { dialog, which ->
                        finish()
                    }.show()
            }
            is FinishQuizFragment -> {
                (supportFragmentManager.fragments.last() as FinishQuizFragment).saveTryToDB()
                finish()
                true
            }
            else -> {
                finish()
            }
        }
    }


}
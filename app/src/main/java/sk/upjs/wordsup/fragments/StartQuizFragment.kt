package sk.upjs.wordsup.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import sk.upjs.wordsup.R
import sk.upjs.wordsup.dao.quiz.QuizWithWords
import sk.upjs.wordsup.dao.wordinfo.WordInfo
import sk.upjs.wordsup.dao.wordinfo.WordsDefinitionViewModel

@AndroidEntryPoint
class StartQuizFragment : Fragment() {
    private lateinit var quiz: QuizWithWords
    private var wordInfo = mutableListOf<WordInfo>()
    private var wasStartedQuiz = false;

    private val viewModel: WordsDefinitionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            quiz = it.getSerializable("quiz") as QuizWithWords
        }
        quiz.words = quiz.words.shuffled()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_start_quiz, container, false)
    }

    override fun onResume() {
        super.onResume()

        requireView().findViewById<TextView>(R.id.quiz_name).text = quiz.quiz.name
        requireView().findViewById<TextView>(R.id.words_number).text =
            quiz.words.size.toString() + " WORDS"

        requireView().findViewById<MaterialButton>(R.id.start_quiz).setOnClickListener {
            openFragment(QuizPlayFragment.newInstance(wordInfo, quiz))
            wasStartedQuiz = true
        }

        viewModel.getWordsInfos(quiz.words)
        viewModel.wordsInfos.observe(this) { list ->
            wordInfo = list
            requireView().findViewById<MaterialButton>(R.id.start_quiz).isEnabled = true
            requireView().findViewById<MaterialButton>(R.id.start_quiz).text =resources.getString(R.string.start_quiz)
            requireView().findViewById<ProgressBar>(R.id.loading_circle).visibility = View.GONE;
        }

        if (wasStartedQuiz) {
            requireView().findViewById<ProgressBar>(R.id.loading_circle).visibility = View.GONE;
        }
    }

    fun openFragment(fragment: Fragment?) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment!!)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {
        @JvmStatic
        fun newInstance(quiz: QuizWithWords) = StartQuizFragment().apply {
            arguments = Bundle().apply {
                putSerializable("quiz", quiz)
            }
        }
    }
}
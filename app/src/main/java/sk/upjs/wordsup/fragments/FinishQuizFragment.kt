@file:Suppress("DEPRECATION", "UNCHECKED_CAST")

package sk.upjs.wordsup.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.button.MaterialButton
import sk.upjs.wordsup.prefs.Prefs
import sk.upjs.wordsup.R
import sk.upjs.wordsup.dao.quiz.QuizWithWords
import sk.upjs.wordsup.dao.tries.Try
import sk.upjs.wordsup.dao.tries.TryViewModel
import java.text.SimpleDateFormat
import java.util.*

class FinishQuizFragment : Fragment() {
    private lateinit var answers: MutableList<Int>
    private lateinit var quiz: QuizWithWords

    private lateinit var correctNumber: TextView
    private lateinit var completionPercentage: TextView
    private lateinit var skippedNumber: TextView
    private lateinit var incorrectAnswers: TextView

    private lateinit var doneButton: MaterialButton

    private val viewModel: TryViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            answers = it.getSerializable("answers") as MutableList<Int>
            quiz = it.getSerializable("quiz") as QuizWithWords
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_finish_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        correctNumber = view.findViewById(R.id.correct_number)
        completionPercentage = view.findViewById(R.id.completion_percentage)
        skippedNumber = view.findViewById(R.id.skipped_number)
        incorrectAnswers = view.findViewById(R.id.incorrect_answers)
        doneButton = view.findViewById(R.id.done_button)

        correctNumber.text = resources.getString(R.string.number_words, answers.count { it == 1 })
        completionPercentage.text = resources.getString(R.string.completion_percentage,
            (answers.count { it >= 1 } / answers.size.toDouble()).times(100).toInt())
        skippedNumber.text = resources.getString(R.string.skipped_number, answers.count { it == 0 })
        incorrectAnswers.text =
            resources.getString(R.string.skipped_number, answers.count { it < 0 })

        doneButton.setOnClickListener {
            saveTryToDB()
            requireActivity().finish()
        }

        Prefs.getInstance(this.requireContext()).date =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    fun saveTryToDB() {
        val tries = Try(0, quiz.quiz.quizId, ((answers.count{it == 1}.div(answers.size.toDouble())).times(100)).toInt(), Date())
        viewModel.insert(listOf(tries))
    }

    companion object {
        @JvmStatic
        fun newInstance(answers: MutableList<Int>, quiz: QuizWithWords) =
            FinishQuizFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("answers", answers as java.io.Serializable)
                    putSerializable("quiz", quiz)
                }
            }
    }
}
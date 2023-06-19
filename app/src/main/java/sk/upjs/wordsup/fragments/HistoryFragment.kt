package sk.upjs.wordsup.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButtonToggleGroup
import sk.upjs.wordsup.Prefs
import sk.upjs.wordsup.R
import sk.upjs.wordsup.dao.tries.TryViewModel
import sk.upjs.wordsup.dao.tries.TryWithQuizAdapter


class HistoryFragment : Fragment() {
    private lateinit var totallyQuizCompleted: TextView
    private lateinit var totallyWordAnswered: TextView
    private lateinit var toggleButton: MaterialButtonToggleGroup

    private lateinit var recyclerView: RecyclerView

    private var adapter = TryWithQuizAdapter()

    private val viewModel: TryViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        totallyQuizCompleted = view.findViewById(R.id.totally_quiz_completed)
        totallyWordAnswered = view.findViewById(R.id.totally_word_answered)
        recyclerView = view.findViewById(R.id.recycler_view_tries)
        toggleButton = view.findViewById(R.id.toggleButton)

        requireView().findViewById<TextView>(R.id.name_text_view).text =
            Prefs.getInstance(requireContext()).name

        recyclerView.adapter = adapter

        toggleButton.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            // bez toho nechce
            if (!isChecked) return@addOnButtonCheckedListener
            val adapter = recyclerView.adapter as TryWithQuizAdapter

            when (checkedId) {
                R.id.last_day_button -> adapter.filter(TryWithQuizAdapter.FilterType.LAST_DAY)
                R.id.last_week_button -> adapter.filter(TryWithQuizAdapter.FilterType.LAST_WEEK)
                R.id.last_month_button -> adapter.filter(TryWithQuizAdapter.FilterType.LAST_MONTH)
                else -> adapter.filter(TryWithQuizAdapter.FilterType.ALL)
            }
        }

        viewModel.allTries.observe(this) {
            adapter.modifyList(it.toMutableList())

            if (savedInstanceState != null) {
                // bez toho nechce
                toggleButton.clearChecked()
                toggleButton.check(savedInstanceState.getInt("checkedButton"))
            }

            totallyQuizCompleted.text = it.size.toString()
            val sum = it.fold(0.0) { acc, tryItem ->
                acc + tryItem.attempt.percentage.toDouble() / 100 * tryItem.words.size
            }
            totallyWordAnswered.text = "~ "+ sum.toInt().toString()
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // https://stackoverflow.com/questions/37618738/how-to-check-if-a-lateinit-variable-has-been-initialized
        if (this::toggleButton.isInitialized) {
            outState.putInt("checkedButton", toggleButton.checkedButtonId)
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = HistoryFragment().apply {}
    }


}
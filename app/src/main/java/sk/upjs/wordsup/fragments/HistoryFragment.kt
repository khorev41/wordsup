package sk.upjs.wordsup.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import sk.upjs.wordsup.prefs.Prefs
import sk.upjs.wordsup.R
import sk.upjs.wordsup.dao.tries.TryViewModel
import sk.upjs.wordsup.dao.tries.TryWithQuizAdapter


class HistoryFragment : Fragment() {
    private lateinit var totallyQuizCompleted: TextView
    private lateinit var totallyWordAnswered: TextView

    private lateinit var recyclerView: RecyclerView

    private var adapter = TryWithQuizAdapter()

    private var checkedId = 0

    private val viewModel: TryViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        totallyQuizCompleted = view.findViewById(R.id.totally_quiz_completed)
        totallyWordAnswered = view.findViewById(R.id.totally_word_answered)
        recyclerView = view.findViewById(R.id.recycler_view_tries)
        val buttons = listOf<Pair<MaterialButton, TryWithQuizAdapter.FilterType>>(
            Pair(view.findViewById(R.id.last_day_button), TryWithQuizAdapter.FilterType.LAST_DAY),
            Pair(view.findViewById(R.id.last_week_button), TryWithQuizAdapter.FilterType.LAST_WEEK),
            Pair(
                view.findViewById(R.id.last_month_button), TryWithQuizAdapter.FilterType.LAST_MONTH
            )
        )



        requireView().findViewById<TextView>(R.id.name_text_view).text =
            Prefs.getInstance(requireContext()).name

        recyclerView.adapter = adapter

        val adapter = recyclerView.adapter as TryWithQuizAdapter
        buttons.forEach {
            it.first.addOnCheckedChangeListener { button, isChecked ->
                checkedId = if (isChecked) {
                    adapter.filter(it.second)
                    button.id
                } else {
                    adapter.filter(TryWithQuizAdapter.FilterType.ALL)
                    -1
                }
            }
        }

        if (savedInstanceState != null) {
            checkedId = savedInstanceState.getInt("checkedButton")
        }

        viewModel.allTries.observe(this) {
            adapter.modifyList(it.toMutableList())
            //https://stackoverflow.com/questions/44429419/difference-between-fold-and-reduce-in-kotlin-when-to-use-which
            totallyQuizCompleted.text = it.size.toString()
            val sum = it.fold(0.0) { acc, tryItem ->
                acc + tryItem.attempt.percentage.toDouble() / 100 * tryItem.words.size
            }
            totallyWordAnswered.text = resources.getString(R.string.aproximated_totally_word_answered, sum.toInt())
        }


    }

    override fun onResume() {
        super.onResume()

        //toto musi byt
        when (checkedId) {
            R.id.last_day_button -> adapter.filter(TryWithQuizAdapter.FilterType.LAST_DAY)
            R.id.last_week_button -> adapter.filter(TryWithQuizAdapter.FilterType.LAST_WEEK)
            R.id.last_month_button -> adapter.filter(TryWithQuizAdapter.FilterType.LAST_MONTH)
            else -> adapter.filter(TryWithQuizAdapter.FilterType.ALL)
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
            outState.putInt("checkedButton", checkedId)

    }

    companion object {
        @JvmStatic
        fun newInstance() = HistoryFragment().apply {}
    }


}
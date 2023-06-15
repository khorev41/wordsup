package sk.upjs.wordsup.fragments

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sk.upjs.wordsup.Prefs
import sk.upjs.wordsup.R
import sk.upjs.wordsup.dao.quiz.QuizAdapter
import sk.upjs.wordsup.dao.quiz.QuizViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private val viewModel: QuizViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onResume() {
        super.onResume()
        setGreeting(this.requireView())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        setGreeting(view)
        quizView(view)
        return view
    }

    private fun quizView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_quiz)
        recyclerView.layoutManager =
            GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)

        val adapter = QuizAdapter()
        recyclerView.adapter = adapter

        viewModel.allQuizzes.observe(viewLifecycleOwner) {
            adapter.submitList(it?.toMutableList())
        }
    }

    private fun setGreeting(view: View) {

        val name = Prefs.getInstance(requireContext()).name
        view.findViewById<TextView>(R.id.greetingTextView).text = resources.getString(R.string.greetings_text, name)

        val target = Prefs.getInstance(requireContext()).target
        val done = Prefs.getInstance(requireContext()).learned
        val percent = ((done.toDouble() / target) * 100).toInt()
        view.findViewById<TextView>(R.id.progressTextView).text = resources.getString(R.string.progress_text, percent)
        view.findViewById<ProgressBar>(R.id.progress_circular).progress = percent
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
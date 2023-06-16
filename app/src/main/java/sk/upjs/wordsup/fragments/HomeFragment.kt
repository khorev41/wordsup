package sk.upjs.wordsup.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sk.upjs.wordsup.Prefs
import sk.upjs.wordsup.R
import sk.upjs.wordsup.dao.quiz.QuizAdapter
import sk.upjs.wordsup.dao.quiz.QuizViewModel

class HomeFragment : Fragment() {
    private val viewModel: QuizViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = requireView().findViewById(R.id.recycler_view_quiz)
    }

    override fun onStart() {
        super.onStart()

        setGreeting(requireView())
        setRecyclerView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.recycler_view_quiz)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRecyclerView()

            recyclerView.layoutManager =
                LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRecyclerView()

            recyclerView.layoutManager = LinearLayoutManager(this.context)
        }
    }

    private fun setRecyclerView() {
        val adapter = QuizAdapter()
        recyclerView.adapter = adapter

        viewModel.allQuizzes.observe(viewLifecycleOwner) {
            adapter.submitList(it?.toMutableList())
        }
    }

    private fun setGreeting(view: View) {

        val name = Prefs.getInstance(requireContext()).name
        view.findViewById<TextView>(R.id.greetingTextView).text =
            resources.getString(R.string.greetings_text, name)

        val target = Prefs.getInstance(requireContext()).target
        val done = Prefs.getInstance(requireContext()).learned
        val percent = ((done.toDouble() / target) * 100).toInt()
        view.findViewById<TextView>(R.id.progressTextView).text =
            resources.getString(R.string.progress_text, percent)
        view.findViewById<ProgressBar>(R.id.progress_circular).progress = percent
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment().apply {
        }
    }
}
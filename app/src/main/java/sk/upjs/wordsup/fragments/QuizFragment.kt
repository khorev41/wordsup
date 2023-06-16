package sk.upjs.wordsup.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import sk.upjs.wordsup.R
import sk.upjs.wordsup.dao.quiz.QuizAdapter
import sk.upjs.wordsup.dao.quiz.QuizViewModel


class QuizFragment : Fragment() {
    private val viewModel: QuizViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = requireView().findViewById(R.id.recycler_view_quiz)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onStart() {
        super.onStart()
        val adapter = QuizAdapter()
        recyclerView.adapter = adapter

        viewModel.allQuizzes.observe(viewLifecycleOwner) {
            adapter.submitList(it?.toMutableList())
        }

        requireView().findViewById<ImageView>(R.id.add_quiz).setOnClickListener {
            // run AddQuizActivity
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = QuizFragment().apply {}
    }


}
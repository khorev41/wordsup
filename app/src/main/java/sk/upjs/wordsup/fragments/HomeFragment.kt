package sk.upjs.wordsup.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import sk.upjs.wordsup.EditQuizActivity
import sk.upjs.wordsup.R
import sk.upjs.wordsup.dao.quiz.Quiz
import sk.upjs.wordsup.dao.quiz.QuizAdapter
import sk.upjs.wordsup.dao.quiz.QuizViewModel
import sk.upjs.wordsup.dao.quiz.QuizWithWords
import sk.upjs.wordsup.prefs.Prefs

class HomeFragment : Fragment() {
    private val viewModel: QuizViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var addButton: MaterialButton
    val adapter = QuizAdapter()

    //direction depending on orientation
    private val itemTouchHelper by lazy {
        val callback = object : ItemTouchHelper.SimpleCallback(
            1, if (resources.configuration.orientation == 1) {
                ItemTouchHelper.LEFT
            } else {
                ItemTouchHelper.UP
            }
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedQuiz: QuizWithWords =
                    adapter.currentList[viewHolder.adapterPosition] as QuizWithWords

                val position = viewHolder.adapterPosition
                adapter.deleteOn(position)
                viewModel.deleteQuiz(deletedQuiz)

                Snackbar.make(
                    recyclerView, "Deleted " + deletedQuiz.quiz.name, Snackbar.LENGTH_LONG
                ).setAction("Undo") {
                    adapter.addItem(deletedQuiz)
                    viewModel.insertQuizWithWord(deletedQuiz)
                }.show()
            }
        }
        ItemTouchHelper(callback)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (this::recyclerView.isInitialized) {
            outState.putInt(
                "position",
                (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recycler_view_quiz)
        addButton = view.findViewById(R.id.add_quiz_button)

        setGreeting(requireView())

        recyclerView.adapter = adapter

        if (savedInstanceState != null) {
            val position = savedInstanceState.getInt("position")
            recyclerView.scrollToPosition(position)
        }

        itemTouchHelper.attachToRecyclerView(recyclerView)

        viewModel.allQuizzes.observe(viewLifecycleOwner) {
            adapter.submitList(it?.toMutableList())
        }

    }


    override fun onStart() {
        super.onStart()

        addButton.setOnClickListener {
            val intent = Intent(it.context, EditQuizActivity::class.java)
            intent.putExtra("quiz", QuizWithWords(Quiz(0, ""), listOf()))
            it.context.startActivity(intent)
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
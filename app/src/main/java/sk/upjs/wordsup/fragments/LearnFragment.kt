package sk.upjs.wordsup.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import com.google.android.material.snackbar.Snackbar
import sk.upjs.wordsup.R
import sk.upjs.wordsup.dao.word.Word
import sk.upjs.wordsup.dao.word.WordListAdapter
import sk.upjs.wordsup.dao.word.WordViewModel


class LearnFragment : Fragment() {

    private val viewModel: WordViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var searchBar: SearchBar
    private lateinit var filteredRecyclerView: RecyclerView

    private var adapter = WordListAdapter(R.drawable.rounded_border_word)
    private var filteredAdapter = WordListAdapter(R.drawable.rounded_border_word_dark)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = requireView().findViewById(R.id.recycler_view_words)
        filteredRecyclerView = requireView().findViewById(R.id.recycler_view_words_filtered)
        searchView = requireView().findViewById(R.id.word_search_view)
        searchBar = requireView().findViewById(R.id.search_bar)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_learn, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onStart() {
        super.onStart()
        setRecyclerView()


        searchView.editText.setOnEditorActionListener { v, actionId, event ->
            searchBar.text = searchView.text
            (filteredRecyclerView.adapter as WordListAdapter).filter.filter(searchBar.text)
            (recyclerView.adapter as WordListAdapter).filter.filter(searchBar.text)
            adapter.notifyDataSetChanged()
            filteredAdapter.notifyDataSetChanged()
            false
        }
        searchView.editText.doOnTextChanged { text, start, before, count ->
            searchBar.text = searchView.text
            (filteredRecyclerView.adapter as WordListAdapter).filter.filter(searchView.text)
            (recyclerView.adapter as WordListAdapter).filter.filter(searchView.text)
            adapter.notifyDataSetChanged()
            filteredAdapter.notifyDataSetChanged()
        }
        searchView.editText.setOnFocusChangeListener { view, b ->
            requireActivity().requireViewById<BottomNavigationView>(R.id.bottom_navigation).isVisible =
                !requireActivity().requireViewById<BottomNavigationView>(R.id.bottom_navigation).isVisible
        }




    }


    private fun setRecyclerView() {

        recyclerView.adapter = adapter
        filteredRecyclerView.adapter = filteredAdapter

        viewModel.allWords.observe(viewLifecycleOwner) {
            adapter.submitList(it?.toMutableList())
            filteredAdapter.submitList(it.toMutableList())
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) = LearnFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }
}
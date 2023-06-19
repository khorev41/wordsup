package sk.upjs.wordsup.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import sk.upjs.wordsup.R
import sk.upjs.wordsup.dao.word.WordListAdapter
import sk.upjs.wordsup.dao.word.WordViewModel

@AndroidEntryPoint
class LearnFragment : Fragment() {

    private val viewModel: WordViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView


    private var adapter = WordListAdapter(R.drawable.rounded_border_word)

    private var search = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = WordListAdapter(R.drawable.rounded_border_word)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("search", search)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = requireView().findViewById(R.id.recycler_view_words)
        searchView = requireView().findViewById(R.id.search_view)

        recyclerView.adapter = adapter

        viewModel.allWords.observe(viewLifecycleOwner) {
            adapter.modifyList(it)
            if (savedInstanceState != null) {
                searchView.setQuery(savedInstanceState.getString("search"),true)
                (recyclerView.adapter as WordListAdapter).filter(savedInstanceState.getString("search"))
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                (recyclerView.adapter as WordListAdapter).filter(newText)
                search = newText
                return false
            }
        })

        searchView.setOnQueryTextFocusChangeListener { view, b ->
            requireActivity().requireViewById<BottomNavigationView>(R.id.bottom_navigation).isVisible =
                !requireActivity().requireViewById<BottomNavigationView>(R.id.bottom_navigation).isVisible
        }
        searchView.setOnClickListener { searchView.isIconified = false }
//        searchView.setOnFocusChangeListener { view, b ->
//            requireActivity().requireViewById<BottomNavigationView>(R.id.bottom_navigation).isVisible =
//                !requireActivity().requireViewById<BottomNavigationView>(R.id.bottom_navigation).isVisible
//        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_learn, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = LearnFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }
}
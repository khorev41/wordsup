package sk.upjs.wordsup.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import sk.upjs.wordsup.R
import sk.upjs.wordsup.dao.word.Word
import sk.upjs.wordsup.dao.wordinfo.DefinitionAdapter
import sk.upjs.wordsup.dao.wordinfo.WordInfo


class WordFragment : Fragment() {

    private lateinit var word: Word
    private lateinit var wordInfo: WordInfo

    private val mediaPlayer = MediaPlayer()
    private var isPrepared = false

    private lateinit var wordTextView: TextView
    private lateinit var phoneticTextView: TextView
    private lateinit var listenButton: Button

    private lateinit var recyclerView: RecyclerView
    private var adapter = DefinitionAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            word = it.getSerializable("word") as Word
            wordInfo = it.getSerializable("wordInfo") as WordInfo
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_word, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wordTextView = requireView().findViewById(R.id.word_string)
        phoneticTextView = requireView().findViewById(R.id.phonetic)
        listenButton = requireView().findViewById(R.id.listen_button)
        recyclerView = requireView().findViewById(R.id.definition_recycler_view)

        if (!wordInfo.phonetic.audio.isNullOrBlank()) {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(wordInfo.phonetic.audio)
            mediaPlayer.prepareAsync()

            mediaPlayer.setOnCompletionListener {
                mediaPlayer.reset()
                mediaPlayer.setDataSource(wordInfo.phonetic.audio)
                mediaPlayer.prepareAsync()
            }
            mediaPlayer.setOnPreparedListener {
                isPrepared = true
            }
        }else{
            listenButton.isVisible = false
        }
    }

    override fun onResume() {
        super.onResume()

        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        adapter.submitList(wordInfo.definitions)

        wordTextView.text = word.word
        phoneticTextView.text =
            if (wordInfo.phonetic.text.isNullOrBlank()) getString(R.string.no_phonetics) else wordInfo.phonetic.text

        listenButton.setOnClickListener {
            try {
                if(isPrepared){
                    mediaPlayer.start()
                    isPrepared = false
                }

            }catch (e: Exception){
                Log.e("PLAYER ERROR", e.toString())
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(word: Word, wordInfo: WordInfo) = WordFragment().apply {
            arguments = Bundle().apply {
                putSerializable("word", word)
                putSerializable("wordInfo", wordInfo)
            }
        }
    }
}
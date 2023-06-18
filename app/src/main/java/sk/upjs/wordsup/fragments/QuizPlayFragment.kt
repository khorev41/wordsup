@file:Suppress("DEPRECATION", "UNCHECKED_CAST")

package sk.upjs.wordsup.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.*
import com.google.android.material.button.MaterialButton
import sk.upjs.wordsup.Prefs
import sk.upjs.wordsup.R
import sk.upjs.wordsup.dao.quiz.QuizWithWords
import sk.upjs.wordsup.dao.word.Word
import sk.upjs.wordsup.dao.wordinfo.WordInfo
import java.io.Serializable

class QuizPlayFragment : Fragment() {
    private var counter = 0
    private var mapOfDefinitions = mutableMapOf<Int, Int>()
    private lateinit var view: View
    private lateinit var quiz: QuizWithWords
    private lateinit var wordInfo: MutableList<WordInfo>
    private lateinit var words: List<Word>
    private lateinit var shuffled: List<Word>
    private var listOfWordLayout = listOf<ConstraintLayout>()
    private var listOfWordCheckMark = listOf<ImageView>()
    private var listOfWordTextView = listOf<TextView>()
    private var wordsTry = MutableList(4) { 0 }
    private lateinit var answers: MutableList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            quiz = arguments?.getSerializable("quiz") as QuizWithWords
            wordInfo = arguments?.getSerializable("wordInfo") as MutableList<WordInfo>
            words = quiz.words
        }
        answers = MutableList(words.size) { 0 }
        shuffled = getRandomList(words[counter])
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_quiz_play, container, false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("quiz", quiz)
        outState.putSerializable("wordInfo", wordInfo as Serializable)
        outState.putSerializable("words", words as Serializable)
        outState.putSerializable("mapOfDefinitions", mapOfDefinitions as Serializable)
        outState.putInt("counter", counter)
        outState.putSerializable("shuffled", shuffled as Serializable)
        outState.putSerializable("wordsTry", wordsTry as Serializable)
        outState.putSerializable("answers", answers as Serializable)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            quiz = it.getSerializable("quiz") as QuizWithWords
            wordInfo = it.getSerializable("wordInfo") as MutableList<WordInfo>
            words = quiz.words
            counter = it.getInt("counter")
            mapOfDefinitions = it.getSerializable("mapOfDefinitions") as MutableMap<Int, Int>
            shuffled = it.getSerializable("shuffled") as List<Word>
            wordsTry = it.getSerializable("wordsTry") as MutableList<Int>
            answers = it.getSerializable("answers") as MutableList<Int>
        }
        initializeButtonsAfterStateRestored(wordsTry)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onStart() {
        super.onStart()
        val nextButton = view.findViewById<MaterialButton>(R.id.nextButton)

        setDefinitions(counter)
        setWords(shuffled)

        nextButton.setOnClickListener {


            unlockOtherButton()
            counter++
            if (counter == words.size) {
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(
                    R.id.fragment_container, FinishQuizFragment.newInstance(answers,quiz)
                )
                transaction.addToBackStack(null)
                transaction.setTransition(TRANSIT_FRAGMENT_FADE)
                transaction.remove(this)
                transaction.commit()

            } else {
                shuffled = getRandomList(words[counter])
                setWords(shuffled)

                setDefinitions(counter)

                listOfWordLayout.forEach {
                    it.background = resources.getDrawable(R.drawable.rounded_border_word)
                }
            }
        }
        setListenerToWords()
        changeDefinition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this@QuizPlayFragment.view = view

        listOfWordLayout = listOf<ConstraintLayout>(
            view.findViewById(R.id.word1),
            view.findViewById(R.id.word2),
            view.findViewById(R.id.word3),
            view.findViewById(R.id.word4)
        )
        listOfWordCheckMark = listOf<ImageView>(
            view.findViewById(R.id.word1_checkmark),
            view.findViewById(R.id.word2_checkmark),
            view.findViewById(R.id.word3_checkmark),
            view.findViewById(R.id.word4_checkmark)
        )
        listOfWordTextView = listOf<TextView>(
            view.findViewById(R.id.word1_textview),
            view.findViewById(R.id.word2_textview),
            view.findViewById(R.id.word3_textview),
            view.findViewById(R.id.word4_textview)
        )
    }

    private fun initializeButtonsAfterStateRestored(wordsTry: List<Int>) {
        for (i in 0..3) {
            if (wordsTry[i] == -1) {
                listOfWordLayout[i].setBackgroundResource(R.drawable.rounded_border_word_red)
                listOfWordCheckMark[i].setBackgroundResource(R.drawable.baseline_close_24)
            }
            if (wordsTry[i] == 1) {
                listOfWordLayout[i].setBackgroundResource(R.drawable.rounded_border_word_green)
                listOfWordCheckMark[i].setBackgroundResource(R.drawable.baseline_done_24)

                lockOtherButton()

            }
        }

    }

    private fun unlockOtherButton() {
        listOfWordLayout.forEach {
            it.isEnabled = true
        }
        for (i in 0..3) {
            listOfWordLayout[i].setBackgroundResource(R.drawable.rounded_border_word)
            listOfWordCheckMark[i].setBackgroundResource(0)
        }

        wordsTry = mutableListOf(0, 0, 0, 0)

    }

    private fun setDefinitions(counter: Int) {
        view.findViewById<TextView>(R.id.word_definition_textview).text =
            wordInfo[counter].definitions[0]
        view.findViewById<TextView>(R.id.word_number_textview).text =
            resources.getString(R.string.word_number, counter + 1, quiz.words.size)

        if(wordInfo[counter].definitions.contains(getString(R.string.definition_not_found))){
            view.findViewById<TextView>(R.id.word_definition_textview).text = getString(R.string.definition_not_found_with_word, words[counter])
        }
    }


    private fun setWords(shuffled: List<Word>) {
        val listOfWordTextView = listOf<TextView>(
            view.findViewById(R.id.word1_textview),
            view.findViewById(R.id.word2_textview),
            view.findViewById(R.id.word3_textview),
            view.findViewById(R.id.word4_textview)
        )

        for (i in 0..3) {
            listOfWordTextView[i].text = shuffled[i].word
        }

    }

    private fun getRandomList(word: Word): List<Word> {
        if (words.isEmpty()) return emptyList()

        val shuffledList = words.shuffled().toMutableList()
        shuffledList[shuffledList.indexOf(word)] = shuffledList[0]
        shuffledList[0] = word

        return shuffledList.subList(0, 4).shuffled()

    }

    private fun changeDefinition() {
        view.findViewById<TextView>(R.id.word_definition_textview).setOnClickListener {
            if (mapOfDefinitions[counter] == null) {
                mapOfDefinitions[counter] = 0
            }
            if ((mapOfDefinitions[counter]?.plus(1)
                    ?: 0) >= wordInfo[counter].definitions.size
            ) {
                mapOfDefinitions[counter] = 0
                mapOfDefinitions[counter]?.let { it1 -> mapOfDefinitions.put(counter, it1) }
            } else {
                view.findViewById<TextView>(R.id.word_definition_textview).text =
                    wordInfo[counter].definitions[mapOfDefinitions[counter]?.plus(1)!!]
                mapOfDefinitions[counter]?.let { it1 -> mapOfDefinitions.put(counter, it1 + 1) }
            }
        }
    }

    private fun setListenerToWords() {
        for (i in 0..3) listOfWordLayout[i].setOnClickListener {
            initializeButtonsAfterStateRestored(i)
        }
    }

    private fun initializeButtonsAfterStateRestored(idx: Int) {
        if(wordInfo[counter].definitions.contains(getString(R.string.definition_not_found))){
            answers[counter] = 1
            return
        }

        if (listOfWordTextView[idx].text != words[counter].word) {
            listOfWordLayout[idx].setBackgroundResource(R.drawable.rounded_border_word_red)
            listOfWordCheckMark[idx].setBackgroundResource(R.drawable.baseline_close_24)
            wordsTry[idx] = -1
            answers[counter] = -1
        } else {
            listOfWordLayout[idx].setBackgroundResource(R.drawable.rounded_border_word_green)
            listOfWordCheckMark[idx].setBackgroundResource(R.drawable.baseline_done_24)
            Prefs.getInstance(this.requireContext()).learned =
                Prefs.getInstance(this.requireContext()).learned + 1
            lockOtherButton()
            wordsTry[idx] = 1
            if (!wordsTry.contains(-1)) {
                answers[counter] = 1
            } else {
                answers[counter] = 2
            }
        }
    }

    private fun lockOtherButton() {
        listOfWordLayout.forEach {
            it.isEnabled = false
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(wordInfo: MutableList<WordInfo>, quiz: QuizWithWords) =
            QuizPlayFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("wordInfo", wordInfo as Serializable)
                    putSerializable("quiz", quiz)
                }
            }
    }

}
package sk.upjs.wordsup

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import sk.upjs.wordsup.dao.Quiz
import sk.upjs.wordsup.dao.Word
import sk.upjs.wordsup.dao.wordinfo.WordInfo
import sk.upjs.wordsup.dao.quizWithWords.WordsByQuizViewModel
import sk.upjs.wordsup.rest.RestApi

import kotlin.random.Random

@AndroidEntryPoint
class QuizActivity : AppCompatActivity() {

    private val viewModel: WordsByQuizViewModel by viewModels<WordsByQuizViewModel>()
    private var counter = 0;
    private var list = mutableListOf<Word>()
    private var map: Map<String, WordInfo> = emptyMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        val nextButton = findViewById<MaterialButton>(R.id.nextButton)

        val listOfWordLayout = listOf<ConstraintLayout>(
            findViewById<ConstraintLayout>(R.id.word1),
            findViewById<ConstraintLayout>(R.id.word2),
            findViewById<ConstraintLayout>(R.id.word3),
            findViewById<ConstraintLayout>(R.id.word4)
        )

        val background = listOfWordLayout[0].background

        val quiz = intent.getSerializableExtra("quiz") as Quiz

        viewModel.getWordsByQuiz(quiz).observe(this) {
            list = it.toMutableList()
            setWords(list[0])
            lifecycleScope.launch {
                findViewById<TextView>(R.id.word_definition_textview).text =
                    RestApi.wordsRestDao.getDictionaryData(list[counter].word)[0].meanings?.get(0)?.definitions?.get(0)?.definition.toString()
            }

        }

        findViewById<TextView>(R.id.word_number_textview).setText("WORD " + (counter + 1) + " OF " + quiz.wordsNumber)

        nextButton.setOnClickListener {
            if (counter + 1 == quiz.wordsNumber) {
                setWords(list[counter])
            } else {
                setWords(list[counter])
            }
            counter++
            lifecycleScope.launch {
                findViewById<TextView>(R.id.word_definition_textview).text =
                    RestApi.wordsRestDao.getDictionaryData(list[counter].word)[0].meanings?.get(0)?.definitions?.get(0)?.definition.toString()
            }

            findViewById<TextView>(R.id.word_number_textview).setText("WORD " + (counter + 1) + " OF " + quiz.wordsNumber)
            listOfWordLayout.forEach {
                it.background = background
            }
        }

        setListenerToWords(listOfWordLayout)
    }

    private fun setListenerToWords(listOfWordLayout: List<ConstraintLayout> ) {
        val listOfWordTextView = listOf<TextView>(
            findViewById<TextView>(R.id.word1_textview),
            findViewById<TextView>(R.id.word2_textview),
            findViewById<TextView>(R.id.word3_textview),
            findViewById<TextView>(R.id.word4_textview)
        )

        listOfWordLayout[0].setOnClickListener {
            if (listOfWordTextView[0].text != list[counter].word) {
                listOfWordLayout[0].setBackgroundColor(Color.RED)
            } else {
                listOfWordLayout[0].setBackgroundColor(Color.GREEN)
            }
        }

        listOfWordLayout[1].setOnClickListener {
            if (listOfWordTextView[1].text != list[counter].word) {
                listOfWordLayout[1].setBackgroundColor(Color.RED)
            } else {
                listOfWordLayout[1].setBackgroundColor(Color.GREEN)
            }
        }

        listOfWordLayout[2].setOnClickListener {
            if (listOfWordTextView[2].text != list[counter].word) {
                listOfWordLayout[2].setBackgroundColor(Color.RED)
            } else {
                listOfWordLayout[2].setBackgroundColor(Color.GREEN)
            }
        }

        listOfWordLayout[3].setOnClickListener {
            if (listOfWordTextView[3].text != list[counter].word) {
                listOfWordLayout[3].setBackgroundColor(Color.RED)
            } else {
                listOfWordLayout[3].setBackgroundColor(Color.GREEN)
            }
        }
    }

    private fun setWords(word: Word) {
        val listOfWordTextView = listOf<TextView>(
            findViewById<TextView>(R.id.word1_textview),
            findViewById<TextView>(R.id.word2_textview),
            findViewById<TextView>(R.id.word3_textview),
            findViewById<TextView>(R.id.word4_textview)
        )

        val shuffled = getRandomList(list[counter])

       for (i in 0..3) {
           listOfWordTextView[i].text = shuffled[i].word
       }

    }

    private fun getRandomList(word: Word): List<Word> {
        if (list.isEmpty()) return emptyList()

        var shuffledList = list.shuffled()
        while (shuffledList.indexOfFirst { it.word.equals(word) } > 3){
            shuffledList = list.shuffled()
        }
        return shuffledList.subList(0, 4)
    }
}
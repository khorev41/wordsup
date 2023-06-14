package sk.upjs.wordsup

import Phonetic
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import sk.upjs.wordsup.dao.quiz.QuizWithWords
import sk.upjs.wordsup.dao.word.Word
import sk.upjs.wordsup.dao.wordinfo.WordInfo
import sk.upjs.wordsup.dao.wordinfo.WordsDefinitionViewModel

@Suppress("DEPRECATION")
@AndroidEntryPoint
class QuizActivity : AppCompatActivity() {

    private val viewModel: WordsDefinitionViewModel by viewModels()
    private var counter = 0
    private var words = listOf<Word>()
    private var wordInfo = mutableMapOf<String, WordInfo>()
    private var firstWordInfo = WordInfo(listOf(), Phonetic())
    private var mapOfDefinitions = mutableMapOf<Int, Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        val nextButton = findViewById<MaterialButton>(R.id.nextButton)
        val listOfWordLayout = listOf<ConstraintLayout>(
            findViewById(R.id.word1),
            findViewById(R.id.word2),
            findViewById(R.id.word3),
            findViewById(R.id.word4)
        )
        val background = listOfWordLayout[0].background
        val quiz = intent.getSerializableExtra("quiz") as QuizWithWords
        words = quiz.words

        viewModel.getWordInfo(quiz.words[counter]).observe(this) {
            firstWordInfo = it
            findViewById<TextView>(R.id.word_definition_textview).text =
                firstWordInfo.definitions[counter]
        }
        viewModel.getWordsInfos(words).observe(this) {
            wordInfo = it
        }

        findViewById<TextView>(R.id.word_number_textview).text =
            resources.getString(R.string.word_number, counter+1, quiz.words.size)
        setWords(words[counter])

        nextButton.setOnClickListener {
            counter++
            if (counter == words.size) {
                finish()
            } else {
                setWords(words[counter])
                findViewById<TextView>(R.id.word_number_textview).text =
                    resources.getString(R.string.word_number, counter+1, quiz.words.size)
                findViewById<TextView>(R.id.word_definition_textview).text =
                    wordInfo[words[counter].word]?.definitions?.get(0) ?: ""
                listOfWordLayout.forEach {
                    it.background = background
                }
            }
        }
        setListenerToWords(listOfWordLayout)
        changeDefinition()

    }

    private fun changeDefinition() {
        findViewById<TextView>(R.id.word_definition_textview).setOnClickListener {
            if (mapOfDefinitions[counter] == null) {
                mapOfDefinitions[counter] = 0
            }
            if ((mapOfDefinitions[counter]?.plus(1)
                    ?: 0) >= wordInfo[words[counter].word]?.definitions?.size!!
            ) {
                mapOfDefinitions[counter] = 0
                mapOfDefinitions[counter]?.let { it1 -> mapOfDefinitions.put(counter, it1) }
            } else {
                findViewById<TextView>(R.id.word_definition_textview).text =
                    wordInfo[words[counter].word]?.definitions?.get(
                        mapOfDefinitions[counter]?.plus(1)!!
                    )
                mapOfDefinitions[counter]?.let { it1 -> mapOfDefinitions.put(counter, it1 + 1) }
            }
        }
    }

    private fun setListenerToWords(listOfWordLayout: List<ConstraintLayout>) {
        val listOfWordTextView = listOf<TextView>(
            findViewById(R.id.word1_textview),
            findViewById(R.id.word2_textview),
            findViewById(R.id.word3_textview),
            findViewById(R.id.word4_textview)
        )

        listOfWordLayout[0].setOnClickListener {
            if (listOfWordTextView[0].text != words[counter ].word) {
                listOfWordLayout[0].setBackgroundColor(Color.RED)
            } else {
                listOfWordLayout[0].setBackgroundColor(Color.GREEN)
            }
        }

        listOfWordLayout[1].setOnClickListener {
            if (listOfWordTextView[1].text != words[counter].word) {
                listOfWordLayout[1].setBackgroundColor(Color.RED)
            } else {
                listOfWordLayout[1].setBackgroundColor(Color.GREEN)
            }
        }

        listOfWordLayout[2].setOnClickListener {
            if (listOfWordTextView[2].text != words[counter].word) {
                listOfWordLayout[2].setBackgroundColor(Color.RED)
            } else {
                listOfWordLayout[2].setBackgroundColor(Color.GREEN)
            }
        }

        listOfWordLayout[3].setOnClickListener {
            if (listOfWordTextView[3].text != words[counter].word) {
                listOfWordLayout[3].setBackgroundColor(Color.RED)
            } else {
                listOfWordLayout[3].setBackgroundColor(Color.GREEN)
            }
        }
    }

    private fun setWords(word: Word) {
        val listOfWordTextView = listOf<TextView>(
            findViewById(R.id.word1_textview),
            findViewById(R.id.word2_textview),
            findViewById(R.id.word3_textview),
            findViewById(R.id.word4_textview)
        )

        val shuffled = getRandomList(word)

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
}
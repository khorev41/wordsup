package sk.upjs.wordsup

import sk.upjs.wordsup.dao.word.WordAdapter
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import sk.upjs.wordsup.dao.quiz.QuizViewModel
import sk.upjs.wordsup.dao.quiz.QuizWithWords
import sk.upjs.wordsup.dao.word.Word
import sk.upjs.wordsup.dao.word.WordViewModel

@AndroidEntryPoint
class EditQuizActivity : AppCompatActivity() {

    private lateinit var quiz: QuizWithWords
    private var name = ""

    private lateinit var listView: ListView
    private lateinit var nameTextField: TextInputEditText
    private lateinit var nameLayout: TextInputLayout
    private lateinit var wordTextInput: TextInputEditText
    private lateinit var wordLayout: TextInputLayout
    private lateinit var addButton: Button
    private lateinit var saveButton: Button



    private lateinit var adapter: WordAdapter
    private val quizViewModel: QuizViewModel by viewModels()
    private val wordViewModel: WordViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_quiz)

        quiz = intent.getSerializableExtra("quiz") as QuizWithWords
        adapter = WordAdapter(this.applicationContext,quiz.words.toMutableList())
        name = quiz.quiz.name
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("name", name)
        outState.putSerializable("myquiz", quiz)
        outState.putSerializable("adapter", adapter)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        name = savedInstanceState.getString("name").toString()
        quiz = savedInstanceState.getSerializable("myquiz") as QuizWithWords
        adapter = savedInstanceState.getSerializable("adapter") as WordAdapter
    }


    override fun onResume() {
        super.onResume()
        listView = findViewById(R.id.word_listview)
        nameTextField = findViewById(R.id.name_text_input)
        nameLayout = findViewById(R.id.name_text_layout)
        addButton = findViewById(R.id.add_word_button)
        wordTextInput = findViewById(R.id.word_text_input)
        wordLayout = findViewById(R.id.word_text_layout)
        saveButton = findViewById(R.id.save_quiz_button)

        listView.adapter = adapter
        nameTextField.setText(name)


        addButton.setOnClickListener {
            if (wordTextInput.text.isNullOrBlank()) {
                wordLayout.error = "Field is empty"
            } else {
                if (adapter.getItemList().contains(Word(0, wordTextInput.text.toString()))) {
                    wordLayout.error = "Word is already in quiz"
                } else {
                    adapter.getItemList().add(Word(0, wordTextInput.text.toString()))
                    listView.setSelection(adapter.getItemList().size)
                    wordTextInput.setText("")
                }
            }
            adapter.notifyDataSetChanged()
        }

        wordTextInput.addTextChangedListener {
            wordLayout.error = null
        }

        saveButton.setOnClickListener {
            if (nameTextField.text.isNullOrBlank()) {
                nameLayout.error = "Name is empty"
            } else {
                quiz.words = adapter.getItemList()
                quiz.quiz.name = nameTextField.text.toString()
                quizViewModel.insertQuizWithWords(quiz)
                wordViewModel.deleteWordsFromQuiz(quiz, adapter.getToDelete())
                finish()
            }
        }
    }


}
@file:Suppress("DEPRECATION")

package sk.upjs.wordsup

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import sk.upjs.wordsup.dao.quiz.QuizViewModel
import sk.upjs.wordsup.dao.quiz.QuizWithWords
import sk.upjs.wordsup.dao.word.Word
import sk.upjs.wordsup.dao.word.WordAdapter
import sk.upjs.wordsup.dao.word.WordViewModel


@AndroidEntryPoint
class EditQuizActivity : AppCompatActivity() {

    private lateinit var quiz: QuizWithWords
    private var name = ""

    private lateinit var listView: RecyclerView
    private lateinit var nameTextField: TextInputEditText
    private lateinit var nameLayout: TextInputLayout
    private lateinit var wordTextInput: TextInputEditText
    private lateinit var wordLayout: TextInputLayout
    private lateinit var addButton: Button
    private lateinit var saveButton: Button

    private var flag = true


    private lateinit var adapter: WordAdapter
    private val quizViewModel: QuizViewModel by viewModels()
    private val wordViewModel: WordViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_quiz)


        quiz = intent.getSerializableExtra("quiz") as QuizWithWords

        if (quiz.quiz.quizId == 0L){
            findViewById<TextView>(R.id.quiz_edit).text = getString(R.string.create_quiz)
        }

        adapter = WordAdapter()
        adapter.submitList(quiz.words)
        name = quiz.quiz.name
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("name", name)
        outState.putSerializable("myquiz", quiz)
        outState.putSerializable("adapter", adapter)
        outState.putBoolean("flag",flag)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        flag = savedInstanceState.getBoolean("flag")
        name = savedInstanceState.getString("name").toString()
        quiz = savedInstanceState.getSerializable("myquiz") as QuizWithWords
        adapter = savedInstanceState.getSerializable("adapter") as WordAdapter
    }

    override fun onResume() {
        super.onResume()
        listView = findViewById(R.id.word_recycler_view)
        nameTextField = findViewById(R.id.name_text_input)
        nameLayout = findViewById(R.id.name_text_layout)
        addButton = findViewById(R.id.add_word_button)
        wordTextInput = findViewById(R.id.word_text_input)
        wordLayout = findViewById(R.id.word_text_layout)
        saveButton = findViewById(R.id.save_quiz_button)


        listView.adapter = adapter
        nameTextField.setText(name)

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(1, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedWord: Word = adapter.currentList[viewHolder.adapterPosition]

                val position = viewHolder.adapterPosition
                adapter.deleteItem(position)
                wordViewModel.deleteWordFromQuiz(quiz.quiz, deletedWord)
                flag = false

                Snackbar.make(listView, "Deleted " + deletedWord.word, Snackbar.LENGTH_LONG)
                    .setAction(
                        "Undo"
                    ) {
                        quizViewModel.insertQuizAndWord(quiz.quiz, deletedWord)
                        adapter.addItemAt(position, deletedWord)
                    }.show()
            }

        }).attachToRecyclerView(listView)

        addButton.setOnClickListener {
            flag = false
            if (wordTextInput.text.isNullOrBlank()) {
                wordLayout.error = "Field is empty"
            } else {
                var word = Word(
                    0,
                    wordTextInput.text.toString().replace(Regex("\\s+"), " ").trim().toLowerCase()
                        .capitalize()
                )
                if (adapter.currentList.contains(word)) {
                    wordLayout.error = "Word is already in quiz"
                } else {
                    adapter.addItemAt(0, word)
                    quiz.quiz.name =
                        nameTextField.text.toString().replace(Regex("\\s+"), " ").trim()
                            .toLowerCase().capitalize()

                    // priebezne ukladanie lepsie ako vsetky nakoniec
                    quizViewModel.insertQuizAndWord(quiz.quiz, word)
                    quizViewModel.quizSavedId.observe(this) {
                        quiz.quiz.quizId = it
                    }
                    wordTextInput.setText("")
                }
            }

            //https://stackoverflow.com/questions/36426129/recyclerview-scroll-to-position-not-working-every-time
            listView.post {
                listView.smoothScrollToPosition(0)
            }
        }


        wordTextInput.addTextChangedListener {
            wordLayout.error = null
        }
        nameTextField.addTextChangedListener {
            flag = false

        }

        wordTextInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // TODO
                addButton.performClick()
                true
            } else false
        }

        nameTextField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addButton.performClick()
                true
            } else false
        }

        saveButton.setOnClickListener {
            if (adapter.currentList.size > 3) {
                if (nameTextField.text.isNullOrBlank()) {
                    nameLayout.error = "Name is empty"
                } else {
                    quiz.words = adapter.currentList.toMutableList()
                    finish()
                }
            } else {
                wordLayout.error = "Quiz should have at least 4 words"
            }
        }
    }


    override fun onBackPressed() {
        if(flag){
            finish()
        }else{
            MaterialAlertDialogBuilder(this).setTitle(getString(R.string.are_you_sure))
                .setMessage(getString(R.string.changes_will_not_be_saved))
                .setNegativeButton(getString(R.string.no)) { _, _ ->
                    // Respond to negative button press
                }.setPositiveButton(getString(R.string.yes)) { _, _ ->
                    finish()
                }.show()
        }

    }


}
package sk.upjs.wordsup

import android.os.Bundle
import android.widget.Button
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
import sk.upjs.wordsup.dao.quiz.WordAdapter
import sk.upjs.wordsup.dao.word.Word
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


    private lateinit var adapter: WordAdapter
    private val quizViewModel: QuizViewModel by viewModels()
    private val wordViewModel: WordViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_quiz)

        quiz = intent.getSerializableExtra("quiz") as QuizWithWords
        adapter = WordAdapter()
        adapter.submitList(quiz.words)
        adapter.getItemList().addAll(quiz.words)
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
        listView = findViewById(R.id.word_recycler_view)
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
                    adapter.getItemList().add(0, Word(0, wordTextInput.text.toString()))
                    adapter.submitList(adapter.getItemList())
                    wordTextInput.setText("")
                }
            }
            adapter.notifyDataSetChanged()
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(1, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedWord: Word = adapter.currentList[viewHolder.adapterPosition] as Word

                val position = viewHolder.adapterPosition
                adapter.getToDelete().add(deletedWord)
                adapter.getItemList().removeAt(viewHolder.adapterPosition)
                adapter.submitList(adapter.getItemList())
                adapter.notifyDataSetChanged()


                Snackbar.make(listView, "Deleted " + deletedWord.word, Snackbar.LENGTH_LONG)
                    .setAction(
                        "Undo"
                    ) {
                        adapter.getItemList().add(position, deletedWord)
                        adapter.submitList(adapter.getItemList())
                        adapter.getToDelete().remove(deletedWord)
                        adapter.notifyDataSetChanged()
                    }.show()
            }

        }).attachToRecyclerView(listView)

        wordTextInput.addTextChangedListener {
            wordLayout.error = null
        }

        saveButton.setOnClickListener {
            if(adapter.getItemList().size > 3){
                if (nameTextField.text.isNullOrBlank()) {
                    nameLayout.error = "Name is empty"
                } else {
                    quiz.words = adapter.getItemList()
                    quiz.quiz.name = nameTextField.text.toString()
                    wordViewModel.deleteWordsFromQuiz(quiz, adapter.getToDelete())
                    quizViewModel.insertQuizWithWords(quiz)
                    finish()
                }
            }else{
                wordLayout.error = "Quiz should have at least 4 words"
            }
        }
    }

    override fun onBackPressed() {
        MaterialAlertDialogBuilder(this).setTitle(getString(R.string.are_you_sure))
            .setMessage(getString(R.string.changes_will_not_be_saved))
            .setNegativeButton(getString(R.string.no)) { dialog, which ->
                // Respond to negative button press
            }.setPositiveButton(getString(R.string.yes)) { dialog, which ->
                finish()
            }.show()
    }


}
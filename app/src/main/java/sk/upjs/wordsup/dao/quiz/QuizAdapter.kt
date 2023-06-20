package sk.upjs.wordsup.dao.quiz

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sk.upjs.wordsup.EditQuizActivity
import sk.upjs.wordsup.QuizActivity
import sk.upjs.wordsup.databinding.QuizLayoutBinding

class QuizAdapter :
    ListAdapter<QuizWithWords, QuizAdapter.QuizViewHolder>(DiffCallback) {

    class QuizViewHolder(private val binding: QuizLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(
            quiz: QuizWithWords,
        ) {
            binding.root.setOnClickListener {
                val intent = Intent(it.context, QuizActivity::class.java)
                intent.putExtra("quiz", quiz)
                it.context.startActivity(intent)
            }
            binding.root.setOnLongClickListener {
                val intent = Intent(it.context, EditQuizActivity::class.java)
                intent.putExtra("quiz", quiz)
                it.context.startActivity(intent)
                true
            }

            binding.quizName.text = quiz.quiz.name
            binding.quizNumber.text = "Number of words: ${quiz.words.size}"
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        return QuizLayoutBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
            .let { QuizViewHolder(it) }
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun addItem(item: QuizWithWords) {
        val list = currentList.toMutableList()
        list.add(item)
        submitList(list)
    }

    fun deleteOn(position: Int) {
        val list = currentList.toMutableList()
        list.removeAt(position)
        submitList(list)
    }


    object DiffCallback : DiffUtil.ItemCallback<QuizWithWords>() {
        // ak obe polozky equals
        // ak dva objekty reprezentuju rovnaku polozku
        override fun areItemsTheSame(oldItem: QuizWithWords, newItem: QuizWithWords) =
            oldItem == newItem

        // ak dva objekty maju rovnaky obsah
        override fun areContentsTheSame(oldItem: QuizWithWords, newItem: QuizWithWords) =
            oldItem == newItem

    }

}


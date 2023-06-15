package sk.upjs.wordsup.dao.quiz

import Phonetic
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sk.upjs.wordsup.QuizActivity
import sk.upjs.wordsup.dao.wordinfo.WordInfo
import sk.upjs.wordsup.databinding.QuizLayoutBinding
import sk.upjs.wordsup.rest.RestApi

class QuizAdapter() :
    ListAdapter<QuizWithWords, QuizAdapter.QuizViewHolder>(DiffCallback) {

    class QuizViewHolder(val binding: QuizLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            quiz: QuizWithWords,
        ) {
            binding.root.setOnClickListener {
                val intent = Intent(it.context, QuizActivity::class.java)
                intent.putExtra("quiz", quiz)
                it.context.startActivity(intent)
            }

            binding.quizName.text = quiz.quiz.name
            binding.quizNumber.text = "Number of words: " + quiz.words.size.toString()
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


package sk.upjs.wordsup.dao.quiz

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sk.upjs.wordsup.dao.word.Word
import sk.upjs.wordsup.databinding.WordItemLayoutBinding

class WordAdapter : ListAdapter<Word, WordAdapter.WordViewHolder>(DiffCallback), java.io.Serializable {

    private var itemList = mutableListOf<Word>()
    private var toDelete = mutableListOf<Word>()

    fun getItemList(): MutableList<Word>{
        return itemList
    }
    fun getToDelete(): MutableList<Word> {
        return toDelete
    }

    class WordViewHolder(val binding: WordItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(word: Word) {
            binding.wordString.text = word.word

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        return WordViewHolder(WordItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    object DiffCallback : DiffUtil.ItemCallback<Word>() {
        // ak obe polozky equals
        // ak dva objekty reprezentuju rovnaku polozku
        override fun areItemsTheSame(oldItem: Word, newItem: Word) = oldItem == newItem

        // ak dva objekty maju rovnaky obsah
        override fun areContentsTheSame(oldItem: Word, newItem: Word) = oldItem == newItem

    }

}


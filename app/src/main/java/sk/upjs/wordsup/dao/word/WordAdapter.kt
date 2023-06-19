package sk.upjs.wordsup.dao.word

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sk.upjs.wordsup.databinding.WordItemLayoutBinding

class WordAdapter : ListAdapter<Word, WordAdapter.WordViewHolder>(DiffCallback), java.io.Serializable {

    class WordViewHolder(val binding: WordItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(word: Word) {
            binding.wordString.text = word.word
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        return WordViewHolder(
            WordItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun addItemAt(position: Int,item: Word) {
        val list = currentList.toMutableList()
        list.add(position,item)
        submitList(list)
    }

    fun deleteItem(position: Int) {
        val list = currentList.toMutableList()
        list.removeAt(position)
        submitList(list)
    }


    object DiffCallback : DiffUtil.ItemCallback<Word>() {
        // ak obe polozky equals
        // ak dva objekty reprezentuju rovnaku polozku
        override fun areItemsTheSame(oldItem: Word, newItem: Word) = oldItem == newItem

        // ak dva objekty maju rovnaky obsah
        override fun areContentsTheSame(oldItem: Word, newItem: Word) = oldItem == newItem

    }

}


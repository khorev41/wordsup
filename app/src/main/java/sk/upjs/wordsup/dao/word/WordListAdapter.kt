package sk.upjs.wordsup.dao.word

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sk.upjs.wordsup.WordActivity
import sk.upjs.wordsup.databinding.WordLayoutBinding
import java.util.*

class WordListAdapter(private val background: Int) :
    ListAdapter<Word, WordListAdapter.WordViewHolder>(DiffCallback) {

    private var unfilteredlist = listOf<Word>()

    class WordViewHolder(private val binding: WordLayoutBinding, private val background: Int) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(word: Word) {
            binding.root.setOnClickListener {
                val intent = Intent(it.context, WordActivity::class.java)
                intent.putExtra("word", word)
                it.context.startActivity(intent)
                true
            }

            binding.wordString.text = word.word
            binding.wordString.setBackgroundResource(background)
        }


    }

    fun filter(query: CharSequence?) {
        val list = mutableListOf<Word>()

        // perform the data filtering
        if (!query.isNullOrEmpty()) {
            unfilteredlist.let {
                list.addAll(it.filter {
                    it.word.lowercase(Locale.getDefault()).contains(
                        query.toString().lowercase(Locale.getDefault())
                    ) || it.word.lowercase(Locale.getDefault())
                        .contains(query.toString().lowercase(Locale.getDefault()))
                })
            }
        } else {
            list.addAll(unfilteredlist)
        }

        submitList(list)
    }


    fun modifyList(list: List<Word>) {
        unfilteredlist = list
        submitList(list)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = WordLayoutBinding.inflate(inflater, parent, false)
        return WordViewHolder(binding, background)
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

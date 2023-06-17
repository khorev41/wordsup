package sk.upjs.wordsup.dao.word

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sk.upjs.wordsup.databinding.WordLayoutBinding
import sk.upjs.wordsup.WordActivity
import sk.upjs.wordsup.rest.RestApi
import java.util.*

class WordListAdapter(private val background: Int) :
    ListAdapter<Word, WordListAdapter.WordViewHolder>(DiffCallback), Filterable {

    var filteredList: MutableList<Word>? = null
    var list: MutableList<Word>? = null


    class WordViewHolder(private val binding: WordLayoutBinding, private val background: Int) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            word: Word,
        ) {

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

    override fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {

                val charString = charSequence.toString()

                if (charString.isEmpty()) {

                    filteredList = list
                } else {
                    list?.let {
                        val filterList = arrayListOf<Word>()
                        for (word in list!!) {

                            if (charString.lowercase(Locale.ENGLISH) in word.word.lowercase(
                                    Locale.ENGLISH
                                )
                            ) {
                                filterList.add(word)
                            }

                        }

                        filteredList = filterList
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults,
            ) {
                filteredList = filterResults.values as ArrayList<Word>
                submitList(filteredList as ArrayList<Word>)
            }
        }
    }

    override fun submitList(items: MutableList<Word>?) {
        super.submitList(items)

        if (list == null) {
            list = items
        }

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


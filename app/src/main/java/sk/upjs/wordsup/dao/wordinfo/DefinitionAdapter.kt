package sk.upjs.wordsup.dao.wordinfo


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sk.upjs.wordsup.databinding.WordLayoutBinding

class DefinitionAdapter :
    ListAdapter<String, DefinitionAdapter.DefinitionViewHolder>(DiffCallback) {

    class DefinitionViewHolder(val binding: WordLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            string: String
        ) {
            binding.wordString.text = string
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefinitionViewHolder {
        return WordLayoutBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
            .let { DefinitionViewHolder(it) }
    }

    override fun onBindViewHolder(holder: DefinitionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    object DiffCallback : DiffUtil.ItemCallback<String>() {
        // ak obe polozky equals
        // ak dva objekty reprezentuju rovnaku polozku
        override fun areItemsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem

        // ak dva objekty maju rovnaky obsah
        override fun areContentsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem

    }

}


package sk.upjs.wordsup.dao.tries

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sk.upjs.wordsup.databinding.TryLayoutBinding
import java.text.SimpleDateFormat
import java.util.*

class TryWithQuizAdapter :
    ListAdapter<TryWithQuiz, TryWithQuizAdapter.TryWithQuizViewHolder>(DiffCallback) {

    private var unfilteredlist = listOf<TryWithQuiz>()

    class TryWithQuizViewHolder(val binding: TryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tries: TryWithQuiz) {
            binding.root.setOnClickListener {}
            binding.root.setOnLongClickListener {
                true
            }

            binding.quizName.text = tries.quiz.name
            binding.percentageCorrect.text = "${tries.attempt.percentage}%"
            binding.datetime.text =  SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(tries.attempt.time)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TryWithQuizViewHolder {
        return TryWithQuizViewHolder(TryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TryWithQuizViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object DiffCallback : DiffUtil.ItemCallback<TryWithQuiz>() {
        // ak obe polozky equals
        // ak dva objekty reprezentuju rovnaku polozku
        override fun areItemsTheSame(oldItem: TryWithQuiz, newItem: TryWithQuiz) =
            oldItem == newItem

        // ak dva objekty maju rovnaky obsah
        override fun areContentsTheSame(oldItem: TryWithQuiz, newItem: TryWithQuiz) =
            oldItem == newItem

    }

    fun modifyList(list: List<TryWithQuiz>) {
        unfilteredlist = list
        submitList(list)
    }

    //CHATGPT
    fun filter(filterType: FilterType) {
        val list = mutableListOf<TryWithQuiz>()

        unfilteredlist.let {
            list.addAll(it.filter { triwithquiz ->
                val isMatchingDate = when (filterType) {
                    FilterType.LAST_DAY -> isWithinLastDay(triwithquiz.attempt.time)
                    FilterType.LAST_WEEK -> isWithinLastWeek(triwithquiz.attempt.time)
                    FilterType.LAST_MONTH -> isWithinLastMonth(triwithquiz.attempt.time)
                    else -> true // No date filter applied
                }
                isMatchingDate
            })
        }
        submitList(list)
    }


    private fun isWithinLastDay(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        return date.after(calendar.time)
    }


    private fun isWithinLastWeek(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        return date.after(calendar.time)
    }

    private fun isWithinLastMonth(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.MONTH, -1)
        return date.after(calendar.time)
    }

    enum class FilterType {
        LAST_DAY, LAST_WEEK, LAST_MONTH,ALL
    }

}


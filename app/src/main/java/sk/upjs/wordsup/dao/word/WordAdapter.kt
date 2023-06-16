package sk.upjs.wordsup.dao.word

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import sk.upjs.wordsup.R

class WordAdapter(private val context: Context, private val itemList: MutableList<Word>) : BaseAdapter(), java.io.Serializable {

    private var toDelete = mutableListOf<Word>()

    fun getToDelete(): MutableList<Word> {
        return toDelete
    }
    fun getItemList(): MutableList<Word>{
        return itemList
    }

    override fun getCount(): Int {
        return itemList.size
    }

    override fun getItem(position: Int): Any {
        return itemList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.word_item_layout, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.textViewTitle.text = getItem(position).toString()

        view.findViewById<ImageView>(R.id.word_delete_button).setOnClickListener {
            toDelete.add(getItem(position) as Word)
            itemList.removeAt(position)
            this.notifyDataSetChanged()
        }

        return view
    }

    private class ViewHolder(view: View) {
        val textViewTitle: TextView = view.findViewById(R.id.word_string)


    }
}

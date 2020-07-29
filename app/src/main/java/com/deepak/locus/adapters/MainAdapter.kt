package com.deepak.locus.adapters

import com.deepak.locus.ui.BaseViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.deepak.locus.R
import com.deepak.locus.data.model.DataItem
import com.deepak.locus.ui.ChoiceViewHolder
import com.deepak.locus.ui.CommentViewHolder
import com.deepak.locus.ui.ImageViewHolder
import com.deepak.locus.ui.OnItemClickListener

class MainAdapter(private val listItem: List<DataItem>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<BaseViewHolder>() {

    var items: ArrayList<DataItem> = listItem as ArrayList<DataItem>
    var itemClickListener: OnItemClickListener = listener

    init {
        items.addAll(listItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            PHOTO -> {
                ImageViewHolder (
                    layoutInflater.inflate(R.layout.item_image, parent, false), itemClickListener)
            }
            SINGLE_CHOICE -> {
                ChoiceViewHolder (
                    layoutInflater.inflate(R.layout.item_choice_option, parent, false), itemClickListener)
            }

            else -> {
                CommentViewHolder (
                    layoutInflater.inflate(R.layout.item_comment, parent, false), itemClickListener)
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        listItem[position].apply {
            return when (type?.toUpperCase()) {
                "PHOTO" -> PHOTO
                "SINGLE_CHOICE" -> SINGLE_CHOICE
                "COMMENT" -> COMMENT
                else -> 0
            }
        }
    }

    override fun getItemCount() = listItem.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindView(items[position])

    }

    companion object {
        const val PHOTO: Int = 0
        const val SINGLE_CHOICE: Int = 1
        const val COMMENT: Int = 2

    }
}
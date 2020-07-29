package com.deepak.locus.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.deepak.locus.data.model.DataItem


abstract class BaseViewHolder(itemView: View, protected var itemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {

    abstract fun bindView(item: DataItem)

}
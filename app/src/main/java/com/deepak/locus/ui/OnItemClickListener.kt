package com.deepak.locus.ui

import com.deepak.locus.data.model.DataItem

interface OnItemClickListener {

    fun onItemClick(position: Int, item: DataItem)

    fun onCrossClick(position: Int, item: DataItem)
}
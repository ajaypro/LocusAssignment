package com.deepak.locus.data.model

import com.google.gson.JsonObject


data class DataItem(
    var type: String? = null,
    var id: String? = null,
    var title: String? = null,
    val dataMap: JsonObject? = null,
var currentValue: String?){

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("\n Id = $id\n")
        sb.append("Type = $type\n")
        sb.append("Input = " + dataMap.toString())
        return sb.toString()
    }
}

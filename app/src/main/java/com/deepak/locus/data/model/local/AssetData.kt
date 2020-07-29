package com.deepak.locus.data.model.local

import android.content.Context
import com.deepak.locus.data.model.DataItem
import com.google.gson.Gson
import kotlinx.coroutines.Deferred
import java.io.BufferedReader
import java.io.IOException

class AssetData(private val context: Context) : AssetDataContract {

    private val DATA_ASSET_FILE = "response.json"

    override fun getDataFromAssets(): List<DataItem> = readJSONFromAsset(context)

//    private fun getOfflineCurrencies(context: Context): List<DataItem> {
//        var jsonArray: JSONArray? = null
//        try {
//            jsonArray = JSONArray(readJSONFromAsset(context))
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//        val typesData = ArrayList<DataItem>()
//        val optionsList = ArrayList<String>()
//
//
//
//        for (i in 0 until jsonArray!!.length()) {
//
//            try {
//                val type = jsonArray.getJSONObject(i).getString("type")
//                val id = jsonArray.getJSONObject(i).getString("id")
//                val title = jsonArray.getJSONObject(i).getString("title")
//                val dataMap = jsonArray.getJSONObject(i).getJSONArray("dataMap")
//                if(dataMap != null) {
//                    for (j in 0 until dataMap.length())
//                        optionsList.add(dataMap[j].toString())
//                }
//                typesData.add(type, id, title, dataMap[optionsList])
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
//        }
//        return typesData
//    }

    private fun readJSONFromAsset(context: Context): List<DataItem> {
        var inputStream: BufferedReader? = null
        return try {
            inputStream = BufferedReader(context.assets.open(DATA_ASSET_FILE).reader())
            Gson().fromJson(inputStream, Array<DataItem>::class.java).toList()

        } catch (ex: IOException) {
            ex.printStackTrace()
            inputStream?.close()
            emptyList()
        }
    }
}

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

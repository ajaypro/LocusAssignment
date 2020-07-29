package com.deepak.locus.data.model

import com.deepak.locus.data.model.local.AssetData
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataRepostiory(private val data: AssetData) {


    suspend fun getData(): List<DataItem> {
        return withContext(Dispatchers.IO) {
           var dataItemList =  data.getDataFromAssets()
            dataItemList
        }
    }
}
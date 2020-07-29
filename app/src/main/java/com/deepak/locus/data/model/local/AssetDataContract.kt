package com.deepak.locus.data.model.local

import com.deepak.locus.data.model.DataItem

interface AssetDataContract {

    /**
     * To load data from assets
     */
    fun getDataFromAssets(): List<DataItem>

}
package com.deepak.locus.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.deepak.locus.R
import com.deepak.locus.data.model.DataItem
import com.deepak.locus.data.model.DataRepostiory
import com.deepak.locus.data.model.local.AssetData
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var mainViewModel: MainViewModel

    private lateinit var mainViewModelFactory: MainViewModelFactory

    private lateinit var dataRepostiory: DataRepostiory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataRepostiory = DataRepostiory(AssetData(this))

        mainViewModelFactory = MainViewModelFactory(dataRepostiory)

        mainViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)

        mainViewModel.data.observe(this, Observer {
            it?.let {
                dataView.text = it.toString() }
        })


    }

    override fun onItemClick(position: Int, item: DataItem) {
        TODO("Not yet implemented")
    }

    override fun onCrossClick(position: Int, item: DataItem) {
        TODO("Not yet implemented")
    }
}
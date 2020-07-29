package com.deepak.locus.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.deepak.locus.data.model.DataRepostiory
import java.lang.IllegalArgumentException


class MainViewModelFactory(private val dataRepostiory: DataRepostiory): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(dataRepostiory) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
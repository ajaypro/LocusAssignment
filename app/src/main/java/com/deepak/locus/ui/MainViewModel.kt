package com.deepak.locus.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.deepak.locus.data.model.DataItem
import com.deepak.locus.data.model.DataRepostiory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(private val dataRepostiory: DataRepostiory): ViewModel() {

    private var viewModelJob = Job()

    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _data = MutableLiveData<List<DataItem>>()
    val data : LiveData<List<DataItem>> = _data


    init {
      getData()
    }

    private fun getData(){
        viewModelScope.launch {
           _data.value =  dataRepostiory.getData()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
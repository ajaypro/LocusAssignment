package com.deepak.locus.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deepak.locus.data.model.DataItem
import com.deepak.locus.data.model.DataRepostiory
import kotlinx.coroutines.*

class MainViewModel(private val dataRepostiory: DataRepostiory): ViewModel() {

    private var viewModelJob = SupervisorJob()

    //private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _data = MutableLiveData<List<DataItem>>()
    val data : LiveData<List<DataItem>>
            get() = _data


    init {
      //getData()

        viewModelScope.launch {
            _data.value =  dataRepostiory.getData()
            Log.i("VM",data.value.toString())
        }
    }

//    private fun getData(){
//        viewModelScope.launch {
//           _data.value =  dataRepostiory.getData()
//            Log.i("VM",data.value.toString())
//        }
//    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
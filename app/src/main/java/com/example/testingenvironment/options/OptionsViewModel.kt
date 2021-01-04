package com.example.testingenvironment.options

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testingenvironment.database.Album
import com.example.testingenvironment.database.ImageUri
import com.example.testingenvironment.database.ImageUriDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OptionsViewModel(
    private val dataSource: ImageUriDatabaseDao,
) : ViewModel() {

    private var _albumList = MutableLiveData<List<Album>>()
    val albumList: LiveData<List<Album>>
        get() = _albumList

    //coroutines
    private var viewModelJob = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO +  viewModelJob)

    init {
        saveAlbumsIntoList()
    }

    fun saveAlbumsIntoList(){
        ioScope.launch {
            _albumList.postValue(dataSource.getAllAlbums())
        }

    }

    fun albumNameList(): MutableList<String>{
        val list = mutableListOf<String>()

        _albumList.value?.forEach {
            list.add(it.name)
        }

        return list
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
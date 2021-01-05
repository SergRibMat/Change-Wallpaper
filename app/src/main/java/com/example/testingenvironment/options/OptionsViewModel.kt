package com.example.testingenvironment.options

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import com.example.testingenvironment.database.Album
import com.example.testingenvironment.database.ImageUri
import com.example.testingenvironment.database.ImageUriDatabaseDao
import com.example.testingenvironment.worker.SetWallpaperWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class OptionsViewModel(
    private val dataSource: ImageUriDatabaseDao,
) : ViewModel() {

    private var _albumList = MutableLiveData<List<Album>>()
    val albumList: LiveData<List<Album>>
        get() = _albumList

    private var _selectedImagesList = MutableLiveData<List<ImageUri>>()
    val selectedImagesList: LiveData<List<ImageUri>>
        get() = _selectedImagesList


    private var _periodicWorkRequest = MutableLiveData<PeriodicWorkRequest>()
    val periodicWorkRequest: LiveData<PeriodicWorkRequest>
        get() = _periodicWorkRequest

    //coroutines
    private var viewModelJob = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO +  viewModelJob)

    init {
        saveAlbumsIntoList()
    }

    //methods to add
    //empty builder
    //fill builder

    fun inputDataToWorker(): Data{
        val builder = Data.Builder()
        builder?.let {
            selectedImagesList.value?.forEach {
                builder.putString("${it.id}", it.pathToImage)
            }
        }
        return builder.build()
    }

    fun createWorkerClass() = PeriodicWorkRequestBuilder<SetWallpaperWorker>(//ejecutar dentro de observer porque es imprescindible que livedata lleno
        20,
        TimeUnit.MINUTES)
        .setConstraints(setWallpaperWorkerConstraints())
        .setInputData(inputDataToWorker())//cuidado con cuando ejecutas este metodo.
        .build()

    fun setWallpaperWorkerConstraints() = Constraints.Builder()
        .setRequiresBatteryNotLow(true)
        .build()

    fun saveAlbumsIntoList(){
        ioScope.launch {
            _albumList.postValue(dataSource.getAllAlbums())
        }

    }

    fun assigPeriodicWorkRequestToLiveData(){
        _periodicWorkRequest.value = createWorkerClass()
    }

    fun getImagesFromAlbum(name: String){
        ioScope.launch {
            _selectedImagesList.postValue(dataSource.getImagesFromAlbum(1))
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
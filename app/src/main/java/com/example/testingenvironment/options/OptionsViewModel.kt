package com.example.testingenvironment.options

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import com.example.testingenvironment.database.Album
import com.example.testingenvironment.database.ImageUriDatabaseDao
import com.example.testingenvironment.database.OptionsData
import com.example.testingenvironment.worker.SetWallpaperWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class OptionsViewModel(
    private val dataSource: ImageUriDatabaseDao
) : ViewModel() {

    private var _albumList = MutableLiveData<List<Album>>()
    val albumList: LiveData<List<Album>>
        get() = _albumList

    private var _album = MutableLiveData<Album>()
    val album: LiveData<Album>
        get() = _album

    private var _periodicWorkRequest = MutableLiveData<PeriodicWorkRequest>()
    val periodicWorkRequest: LiveData<PeriodicWorkRequest>
        get() = _periodicWorkRequest

    private var _optionsData = MutableLiveData<OptionsData>()
    val optionsData: LiveData<OptionsData>
        get() = _optionsData

    //coroutines
    private var viewModelJob = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO +  viewModelJob)

    init {
        saveAlbumsIntoList()
        saveOptionsDataFirstTime()
        //"Create an object in the viewmodel to hold the state of the options and save it into the database"
    }

    fun getOptionsDataString(): String = "SwitchButton = ${optionsData.value!!.isSelected} album = ${optionsData.value!!.selectedAlbum}" +
            " Time = ${optionsData.value!!.time}"

    fun setTimeAndCurrencyToOptionsData(timeAndCurrency: TimeAndCurrency){
        _optionsData.value?.time = timeAndCurrency.time
        _optionsData.value?.timeUnitInt = timeAndCurrency.currency
    }

    private fun saveOptionsDataFirstTime(){
        ioScope.launch {
            val optionsData = dataSource.getOptionsDataById(1)
            if (optionsData != null){
                _optionsData.postValue(optionsData)
            }else{
                dataSource.insertOptionsData(
                    OptionsData(
                        0L,
                        false,
                        "Empty",
                        45L,
                        0
                    )
                )
                _optionsData.postValue(dataSource.getOptionsDataById(1))
            }

        }

    }

    fun inputDataToWorker(): Data{
        val builder = Data.Builder()
        //add the size to the builder
        builder.putString("album", "${album.value?.albumGroup.toString()}")
        return builder.build()
    }

    fun updateOptionsDataDatabase(){
        ioScope.launch {
            dataSource.updateOptionsData(_optionsData.value!!)
        }
    }

    fun createWorkerClass() = PeriodicWorkRequestBuilder<SetWallpaperWorker>(//ejecutar dentro de observer porque es imprescindible que livedata lleno
        _optionsData.value!!.time,
        getTimeUnit(_optionsData.value!!.timeUnitInt))
        .setConstraints(setWallpaperWorkerConstraints())
        .setInputData(inputDataToWorker())//cuidado con cuando ejecutas este metodo.
        .build()

    fun setWallpaperWorkerConstraints() = Constraints.Builder()
        .setRequiresBatteryNotLow(true)
        .build()

    fun saveAlbumsIntoList(){
        ioScope.launch {
            var albumList = dataSource.getAllAlbums()
            if (albumList.isEmpty()) albumList = listOf(Album("Empty", 0))
            _albumList.postValue(albumList)
        }
    }

    fun assigPeriodicWorkRequestToLiveData(){
        _periodicWorkRequest.value = createWorkerClass()
    }

    fun getAlbumByName(name: String){
        ioScope.launch {
            val album = dataSource.getAlbumByName(name)
            _album.postValue(album)
        }
    }

    fun albumNameList(): MutableList<String>{
        val list = mutableListOf<String>()

        _albumList.value?.forEach {
            list.add(it.name)
        }

        return list
    }

    fun getTimeUnit(timeUnitInt: Int) = when(timeUnitInt) {
        0   -> TimeUnit.MINUTES //*1
        1   -> TimeUnit.DAYS //*60 * 24
        2   -> TimeUnit.HOURS//*60
        else -> TimeUnit.HOURS//*60
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}
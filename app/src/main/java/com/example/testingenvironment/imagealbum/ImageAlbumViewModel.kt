package com.example.testingenvironment.imagealbum

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testingenvironment.database.ImageUriDatabaseDao
import kotlinx.coroutines.Job

class ImageAlbumViewModel(
    private val dataSource: ImageUriDatabaseDao,
    private val application: Application
) : ViewModel(

) {

    val imageUriList = MutableLiveData<List<String>>()
    private var viewModelJob = Job()

    init {

    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun insertImageListToDatabase(){

    }
}
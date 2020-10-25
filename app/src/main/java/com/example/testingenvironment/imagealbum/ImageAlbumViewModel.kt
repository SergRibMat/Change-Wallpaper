package com.example.testingenvironment.imagealbum

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ImageAlbumViewModel : ViewModel() {

    val imageUriList = MutableLiveData<MutableList<Uri>>()


    init {

    }


    override fun onCleared() {
        super.onCleared()
    }
}
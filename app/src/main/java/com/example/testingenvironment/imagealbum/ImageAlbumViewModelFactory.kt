package com.example.testingenvironment.imagealbum

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testingenvironment.database.ImageUriDatabaseDao

class ImageAlbumViewModelFactory(
    private val dataSource: ImageUriDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageAlbumViewModel::class.java)) {
            return ImageAlbumViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
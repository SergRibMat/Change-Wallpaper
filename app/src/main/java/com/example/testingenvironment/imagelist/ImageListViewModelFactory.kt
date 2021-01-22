package com.example.testingenvironment.imagelist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testingenvironment.database.ImageUriDatabaseDao
import com.example.testingenvironment.imagealbum.ImageAlbumViewModel

class ImageListViewModelFactory(
    private val dataSource: ImageUriDatabaseDao,
    private val application: Application,
    private val albumGroup: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageListViewModel::class.java)) {
            return ImageListViewModel(dataSource, application, albumGroup) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
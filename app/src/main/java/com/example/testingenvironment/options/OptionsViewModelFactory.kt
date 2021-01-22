package com.example.testingenvironment.options

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testingenvironment.database.ImageUriDatabaseDao
import com.example.testingenvironment.imagelist.ImageListViewModel

class OptionsViewModelFactory(
    private val dataSource: ImageUriDatabaseDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OptionsViewModel::class.java)) {
            return OptionsViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
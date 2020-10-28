package com.example.testingenvironment.imagealbum

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testingenvironment.database.Album
import com.example.testingenvironment.database.ImageUriDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ImageAlbumViewModel(
    private val dataSource: ImageUriDatabaseDao,
    private val application: Application
) : ViewModel(

) {

    //var declarations
    private var _albumList = MutableLiveData<List<Album>>()
    val albumList: LiveData<List<Album>>
        get() = _albumList

    private var _navigateToImageList = MutableLiveData<Album>()
    val navigateToImageList: LiveData<Album?>
        get() = _navigateToImageList

    val imageUriList = MutableLiveData<List<String>>()

    //coroutines
    private var viewModelJob = Job()
    private val oiScope = CoroutineScope(Dispatchers.IO +  viewModelJob)



    init {
        _navigateToImageList.value = null
        loadAlbumsIntoList()
    }

    fun saveAlbumIntoDatabase(albumName: String){
        oiScope.launch {
            dataSource.insertAlbum(Album(albumName, 8))
            _albumList.postValue(dataSource.getAllAlbums())
        }

    }

    fun loadAlbumsIntoList(){
        oiScope.launch {
            _albumList.postValue(dataSource.getAllAlbums())
        }
    }

    fun navigateToImageListFragment(album: Album){
        _navigateToImageList.value = album
    }

    fun navigateToImageListFragmentCompleted(){
        _navigateToImageList.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun insertImageListToDatabase(){

    }
}

class IsUpdated(val updated: Boolean)
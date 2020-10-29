package com.example.testingenvironment.imagelist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testingenvironment.database.ImageUri
import com.example.testingenvironment.database.ImageUriDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ImageListViewModel(
    private val dataSource: ImageUriDatabaseDao,
    private val application: Application,
    private val albumGroup: Int
) : ViewModel() {

    private var _imageList = MutableLiveData<List<ImageUri>>()
    val imageList: LiveData<List<ImageUri>>
        get() = _imageList

    private var _navigateToDetail = MutableLiveData<ImageUri>()
    val navigateToDetail: LiveData<ImageUri>
        get() = _navigateToDetail



    //coroutines
    private var viewModelJob = Job()
    private val oiScope = CoroutineScope(Dispatchers.IO +  viewModelJob)



    init {
        _navigateToDetail.value = null
        loadImagesIntoList()
    }

    fun loadImagesIntoList(){
        oiScope.launch {
            _imageList.postValue(dataSource.getImagesFromAlbum(albumGroup))
        }
    }

    fun insertImagesIntoDatabase(list: List<String>){
        oiScope.launch {
            dataSource.insertImageUriList(generateImageUrlList(list))
            loadImagesIntoList()
        }
    }

    fun generateImageUrlList(list: List<String>): List<ImageUri>{
        val imageUriList = mutableListOf<ImageUri>()
        list.forEach {
            imageUriList.add(ImageUri(0, "Ser", it, albumGroup))
        }
        return imageUriList
    }


    fun navigateToDetailFragment(imageUri: ImageUri){
        _navigateToDetail.value = imageUri
    }

    fun navigateToDetailFragmentCompleted(){
        _navigateToDetail.value = null
    }

    override fun onCleared() {
        super.onCleared()
    }
}
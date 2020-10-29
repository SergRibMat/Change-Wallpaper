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
        //_imageList.value = null
        //_imageList.value = initTestImageList()
        //addImageUriToList()
        _navigateToDetail.value = null
        //insertImagesIntoDatabase(initTestImageList())
        loadImagesIntoList()
    }

    fun loadImagesIntoList(){
        oiScope.launch {
            _imageList.postValue(dataSource.getImagesFromAlbum(albumGroup))
        }
    }

    fun insertImagesIntoDatabase(list: List<ImageUri>){
        oiScope.launch {
            dataSource.insertImageUriList(list)
            loadImagesIntoList()
        }
    }


    private fun initTestImageList() = listOf(
            ImageUri(1, "Sergio", "path", 1),
            ImageUri(2, "Ribera", "path", 2),
            ImageUri(3, "Mateu", "path", 3),
            ImageUri(3, "Juan", "path", 4),
            ImageUri(5, "Conchin", "path", 5),
            ImageUri(6, "Silvia", "path", 6)
        )


    fun addImageUriToList() =listOf(
            ImageUri(11, "Silvia", "path", 8),
            ImageUri(12, "Pedro", "Path", 8)
        )

    //1-añadir la lista de imagenes a la base de datos
    //2-recoger los datos de la base de datos cuando ha sido añadido
    //3- introducir esos datos en la lista

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
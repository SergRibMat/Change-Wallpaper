package com.example.testingenvironment.imagelist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testingenvironment.database.Album
import com.example.testingenvironment.database.ImageUri
import com.example.testingenvironment.database.ImageUriDatabaseDao

class ImageListViewModel(
    private val dataSource: ImageUriDatabaseDao,
    private val application: Application
) : ViewModel() {

    private var _imageList = MutableLiveData<List<ImageUri>>()
    val imageList: LiveData<List<ImageUri>>
        get() = _imageList

    private var _navigateToDetail = MutableLiveData<ImageUri>()
    val navigateToDetail: LiveData<ImageUri>
        get() = _navigateToDetail



    init {
        //_imageList.value = null
        _imageList.value = initTestImageList()
        //addImageUriToList()
        _navigateToDetail.value = null
    }

    fun loadImagesIntoList(albumGroup: Int){
        dataSource.getImagesFromAlbum(albumGroup)
    }

    private fun initTestImageList() = listOf(
            ImageUri(1, "Sergio", "path", 4),
            ImageUri(1, "Ribera", "path", 4),
            ImageUri(1, "Mateu", "path", 4),
            ImageUri(1, "Juan", "path", 4),
            ImageUri(1, "Conchin", "path", 4),
            ImageUri(1, "Silvia", "path", 4)
        )


    fun addImageUriToList(){
        _imageList.value = listOf(
            ImageUri(1, "Sergio", "path", 4),
            ImageUri(1, "Ribera", "path", 4),
            ImageUri(1, "Mateu", "path", 4),
            ImageUri(1, "Juan", "path", 4),
            ImageUri(1, "Conchin", "path", 4),
            ImageUri(1, "Silvia", "path", 4),
            ImageUri(1, "miki", "path", 4),
            ImageUri(1, "Silvia", "path", 4)
        )


    }

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
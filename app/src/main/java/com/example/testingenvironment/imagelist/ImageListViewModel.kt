package com.example.testingenvironment.imagelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testingenvironment.database.Album
import com.example.testingenvironment.database.ImageUri

class ImageListViewModel : ViewModel() {

    private var _imageList = MutableLiveData<List<ImageUri>>()
    val imageList: LiveData<List<ImageUri>>
        get() = _imageList

    private var _navigateToDetail = MutableLiveData<ImageUri>()
    val navigateToDetail: LiveData<ImageUri>
        get() = _navigateToDetail



    init {
        //_imageList.value = null
        initTestImageList()
        _navigateToDetail.value = null
    }

    private fun initTestImageList() {
        _imageList.value = listOf(
            ImageUri(1, "Sergio", "path", 4),
            ImageUri(1, "Ribera", "path", 4),
            ImageUri(1, "Mateu", "path", 4),
            ImageUri(1, "Juan", "path", 4),
            ImageUri(1, "Conchin", "path", 4),
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
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
        _imageList.value = null
        _navigateToDetail.value = null
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
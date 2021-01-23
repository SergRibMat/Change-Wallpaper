package sergio.ribera.testingenvironment.imagealbum

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sergio.ribera.testingenvironment.MainActivity
import sergio.ribera.testingenvironment.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class ImageAlbumViewModel(
    private val dataSource: ImageUriDatabaseDao,
    private val application: Application
) : ViewModel(

) {

    //livedata declarations
    private var _albumList = MutableLiveData<List<Album>>()
    val albumList: LiveData<List<Album>>
        get() = _albumList

    private var _albumWithImageList = MutableLiveData<List<AlbumWithImages>>()
    val albumWithImageList: LiveData<List<AlbumWithImages>>
        get() = _albumWithImageList


    private var _navigateToImageList = MutableLiveData<Album>()
    val navigateToImageList: LiveData<Album?>
        get() = _navigateToImageList

    //variable declarations
    var albumGroup: Int = 1


    //coroutines
    private var viewModelJob = Job()
    private val oiScope = CoroutineScope(Dispatchers.IO + viewModelJob)



    init {
        _navigateToImageList.value = null
        loadAlbumWithImagesIntoList()
    }



    fun loadAlbumWithImagesIntoList(){
        oiScope.launch {
            _albumWithImageList.postValue(dataSource.getAllAlbumWithImage().toList())
        }
    }

    fun saveAlbumIntoDatabase(albumName: String){
        oiScope.launch {
             dataSource.insertAlbum(Album(albumName, 0))
        }
    }

    fun loadAlbumsIntoList(){
        oiScope.launch {
            _albumList.postValue(dataSource.getAllAlbums())
            loadAlbumWithImagesIntoList()
        }
    }

    fun navigateToImageListFragment(album: Album){
        _navigateToImageList.value = album
    }

    fun navigateToImageListFragmentCompleted(){
        _navigateToImageList.value = null
    }



    fun deleteAlbum(albumGroup: Int){
        oiScope.launch {
            //aqui eliminar las imagenes una a una
            deleteFileImages(albumGroup)
            dataSource.deleteAlbumByAlbumGroup(albumGroup)
            loadAlbumsIntoList()
        }
    }

    suspend fun deleteFileImages(albumGroup: Int){
        val imagePathList = dataSource.getImagePathsFromAlbum(albumGroup)
        Log.i("ImageAlbumViewModel", "el lenght es ${imagePathList.size}")
        imagePathList.forEach { pathToImage ->
            if (File(pathToImage).delete()) {
                Log.i("ImageAlbumViewModel", "Se ha borrado el archivo con path = $pathToImage")
            }else{
                Log.i("ImageAlbumViewModel", "La imagen no se ha borrado")
            }
        }
    }

    fun updateAlbum(album: Album){
        oiScope.launch {
            dataSource.updateAlbum(album)
            loadAlbumsIntoList()
        }
    }

    fun loadImagesIntoList(albumGroup: Int): List<ImageUri>{
        lateinit var imageList: List<ImageUri>
        oiScope.launch {
            imageList = dataSource.getImagesFromAlbum(albumGroup)
        }
        return imageList
    }

    fun updateAlbumById(id: Int, name: String){
        oiScope.launch {
            dataSource.updateAlbumByAlbumGroup(id, name)
            loadAlbumWithImagesIntoList()
        }

    }

    fun insertImagesIntoDatabase(list: List<String>){
        oiScope.launch {
            dataSource.insertImageUriList(generateImageUrlList(list))
            loadAlbumWithImagesIntoList()
        }
    }

    fun generateImageUrlList(list: List<String>): List<ImageUri>{
        val imageUriList = mutableListOf<ImageUri>()
        list.forEach {
            var uriImageStorage = saveImageToInternalStorage(it.toUri(), MainActivity.applicationContext())
            imageUriList.add(ImageUri(0, "", uriImageStorage.toString(), albumGroup))
        }
        return imageUriList
    }

    fun saveImageToInternalStorage(uri: Uri, context: Context): Uri {

        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)

        val wrapper = ContextWrapper(context)

        var file = wrapper.getDir("images", Context.MODE_PRIVATE)


        // Create a file to save the image
        //UUID is used for for creating random file names
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the stream
            stream.flush()

            // Close stream
            stream.close()
        } catch (e: IOException){ // Catch the exception
            e.printStackTrace()
        }

        // Return the saved image uri
        return Uri.parse(file.absolutePath)
    }

    fun albumAlreadyExists(name: String): Boolean{

        _albumWithImageList.value?.forEach {
            if (it.album.name.equals(name, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}

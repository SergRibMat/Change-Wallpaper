package com.example.testingenvironment.imagealbum

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.testingenvironment.database.Album
import com.example.testingenvironment.database.AlbumWithImages
import com.example.testingenvironment.database.ImageUri
import com.example.testingenvironment.database.ImageUriDatabaseDao
import org.junit.Assert.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException



class FakeImageUriDatabaseDao : ImageUriDatabaseDao{

    override fun insertAlbum(album: Album) {
        TODO("Not yet implemented")
    }

    override fun deleteAlbum(album: Album) {
        TODO("Not yet implemented")
    }

    override fun updateAlbum(album: Album) {
        TODO("Not yet implemented")
    }

    override fun insertImageUri(imageUri: ImageUri) {
        TODO("Not yet implemented")
    }

    override fun updateAlbumByAlbumGroup(albumGroup: Int, name: String) {
        TODO("Not yet implemented")
    }

    override fun deleteAlbumByAlbumGroup(albumGroup: Int) {
        TODO("Not yet implemented")
    }

    override fun deleteImageUriById(id: Int) {
        TODO("Not yet implemented")
    }

    override fun getAllImageUri(): List<ImageUri> = listOf(
    ImageUri(1, "name", "path", 1),
    ImageUri(2, "name", "path", 1),
    ImageUri(3, "name", "path", 1),
    ImageUri(4, "name", "path", 2)
    )

    override fun deleteImageUri(imageUri: ImageUri) {
        TODO("Not yet implemented")
    }

    override fun updateImageUri(imageUri: ImageUri) {
        TODO("Not yet implemented")
    }

    override fun getAlbumById(id: Int): Album = Album("First Album", id)

    override fun getImageUriById(id: Int): ImageUri = ImageUri(id, "name", "path", id)

    override fun getImagesFromAlbum(albumGroup: Int): List<ImageUri> = listOf(
        ImageUri(1, "name", "path", albumGroup),
        ImageUri(2, "name", "path", albumGroup),
        ImageUri(3, "name", "path", albumGroup),
        ImageUri(4, "name", "path", albumGroup)
    )

    override fun getAllAlbums(): List<Album> = listOf(
        Album("First Album", 1),
        Album("Second Album", 2)
    )

    @Test
    fun loadAlbumWithImagesIntoList(){ // OK
        var list = mutableListOf<AlbumWithImages>()


        getAllAlbums().forEach { album ->
            list.add(AlbumWithImages(album, emptyList()))
        }

        list.forEach { albumWithImages ->
            albumWithImages.imageList = getAllImageUri()
        }

        var x = list
    }



}

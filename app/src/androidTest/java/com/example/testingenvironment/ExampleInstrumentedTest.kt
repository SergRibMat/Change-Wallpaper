package com.example.testingenvironment

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.testingenvironment.database.Album
import com.example.testingenvironment.database.ImageUri
import com.example.testingenvironment.database.ImageUriDatabase
import com.example.testingenvironment.database.ImageUriDatabaseDao
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private lateinit var imageUriDao: ImageUriDatabaseDao
    private lateinit var db: ImageUriDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, ImageUriDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        imageUriDao = db.imageUriDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetAlbum() {
        val album = Album("myName", 1)
        imageUriDao.insertAlbum(album)
        val albums = imageUriDao.getAllAlbums()
        //assertEquals(albums?.get(0).name, "myName")

        val imageUri = ImageUri(1, "myName", "Path", 1)
        val imageUri2 = ImageUri(2, "myName", "Path", 1)
        val imageUri3 = ImageUri(3, "myName", "Path", 1)
        val imageUri4 = ImageUri(4, "myName", "Path", 2)
        imageUriDao.insertImageUriList(listOf(imageUri, imageUri2, imageUri3))
        imageUriDao.insertImageUri(imageUri4)

        imageUriDao.DeleteAlbumAndImages(album.albumGroup)

        imageUriDao.deleteAlbum(album)

        val imageUriList = imageUriDao.getImagesFromAlbum(1)

        //assertEquals(imageUriList.size, 3)



        //assertEquals(imageUriList.size, 0)

        assertEquals(albums.size, 0)

        //assertEquals(albums?.get(0).name, "myName")



    }



}
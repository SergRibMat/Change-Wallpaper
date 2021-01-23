package sergio.ribera.testingenvironment

import android.content.Context
import androidx.lifecycle.*
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import sergio.ribera.testingenvironment.database.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
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
    private lateinit var imageAlbumViewModel: ViewModel
    private lateinit var context: Context

    @Before
    fun createDb() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, ImageUriDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        imageUriDao = db.imageUriDatabaseDao

        insertAlbums()
        insertImageUri()
        insertOptionsDataTest()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    fun insertAlbums(){
        val album1 = Album("First Album", 0)
        imageUriDao.insertAlbum(album1)

        val album2 = Album("Second Album", 0)
        imageUriDao.insertAlbum(album2)

        val album3 = Album("Third Album", 0)
        imageUriDao.insertAlbum(album3)

        val album4 = Album("Fourth Album", 0)
        imageUriDao.insertAlbum(album4)
    }

    fun insertImageUri(){
        val imageUri = ImageUri(0, "First Uri", "Path", 1)
        val imageUri2 = ImageUri(0, "Second Uri", "Path", 1)
        val imageUri3 = ImageUri(0, "Third Uri", "Path", 1)
        val imageUri4 = ImageUri(0, "Fourth Uri", "Path", 2)
        imageUriDao.insertImageUriList(listOf(imageUri, imageUri2, imageUri3))
        imageUriDao.insertImageUri(imageUri4)
    }

    @Test
    fun getAllImageUriTest(){//OK

        val imageUriList = imageUriDao.getAllImageUri()
        assertEquals(4, imageUriList.size)
    }

    @Test
    fun deleteAlbumTest(){//OK
        imageUriDao.deleteAlbum(Album("Fourth Album", 4))

        imageUriDao.deleteAlbumByAlbumGroup(3)

        val albumList = imageUriDao.getAllAlbums()

        assertEquals(2, albumList.size)
    }

    @Test
    fun deleteImageUriTest(){//OK

        imageUriDao.deleteImageUri(ImageUri(2, "Second Uri", "Path", 1))

        imageUriDao.deleteImageUriById(1)

        val imageUriList = imageUriDao.getAllImageUri()

        assertEquals(2, imageUriList.size)

    }

    @Test
    fun deleteAlbumCascadeTest(){//OK
        imageUriDao.deleteAlbumByAlbumGroup(1)

        val imageUriList = imageUriDao.getAllImageUri()

        assertEquals(1, imageUriList.size)

    }

    @Test
    @Throws(Exception::class)
    fun getAlbum() {//OK

        val albumList = imageUriDao.getAllAlbums()

        assertEquals(albumList.size, 4)

    }

    @Test
    fun getAlbumById(){//OK
        val album = imageUriDao.getAlbumById(4)
        assertEquals(album, Album("Fourth Album", 4))
    }

    @Test
    fun getImageUriById(){//OK
        val imageUri = imageUriDao.getImageUriById(2)
        assertEquals(imageUri, ImageUri(2, "Second Uri", "Path", 1))
    }

    @Test
    fun updateAlbumTest(){//OK
        imageUriDao.updateAlbum(Album("Changed", 4))
        val album = imageUriDao.getAlbumById(4)
        assertEquals(album.name, "Changed")
    }

    fun insertSingleAlbum(name: String, albumGruop: Int){
        imageUriDao.insertAlbum(Album(name, albumGruop))
    }

    fun deleteSingleAlbum(id: Int){
        imageUriDao.deleteAlbumByAlbumGroup(id)
    }

    @Test
    fun updateImageUriTest(){//OK
        imageUriDao.updateImageUri(ImageUri(2, "Second Uri", "Changed", 1))
        val imageUri = imageUriDao.getImageUriById(2)
        assertEquals(imageUri.pathToImage, "Changed")
    }


    @Test
    fun updateAlbumByIdTest(){//OK
        imageUriDao.updateAlbumByAlbumGroup(1, "Sergio")
        val album =imageUriDao.getAlbumById(1)
        assertEquals(album.name, "Sergio")
    }

    @Test
    fun getAlbumByNameTest(){//OK
        val album = imageUriDao.getAlbumByName("Second Album")
        assertEquals(2, album.albumGroup)
    }

    @Test
    fun getOptionsDataTest(){//OK
        val obj = imageUriDao.getOptionsDataById(1L)
        assertEquals(1L, obj.id)
    }

    fun insertOptionsDataTest(){//OK
        imageUriDao.insertOptionsData(OptionsData(0L, true, "Sergio", 20, 0))
    }

    @Test
    fun updateOptionsDataTest(){//OK
        imageUriDao.updateOptionsData(
            OptionsData(
            1L,
                false,
                "Silvia",
                30,
                0
        ))
        val obj = imageUriDao.getOptionsDataById(1L)
        assertEquals(false, obj.isSelected)
    }

    @Test
    fun getImagePathsFromAlbumTest(){//OK
        val list = imageUriDao.getImagePathsFromAlbum(1)
        assertEquals("Path", list[0])
    }
}
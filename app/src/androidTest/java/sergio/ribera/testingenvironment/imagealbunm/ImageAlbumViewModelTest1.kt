package sergio.ribera.testingenvironment.imagealbum

import org.junit.Test

import org.junit.Assert.*

class ImageAlbumViewModelTest {

    @Test
    fun albumAlreadyExistsTest(){
        assertEquals(false, albumAlreadyExists())
    }

    fun albumAlreadyExists(): Boolean {
        val name = "sergio"
        myList().forEach {
            if (it == name){
                return true
            }
        }
        return false
    }

    fun myList(): List<String> = listOf(
        "casa",
        "ventana",
        "cristal"
    )
}
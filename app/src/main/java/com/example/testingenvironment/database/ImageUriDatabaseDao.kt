package com.example.testingenvironment.database

import androidx.room.*

@Dao
interface ImageUriDatabaseDao {

    @Insert
    fun insertAlbum(album: Album)

    @Delete
    fun deleteAlbum(album: Album)

    @Update
    fun updateAlbum(album: Album)

    @Insert
    fun insertImageUri(imageUri: ImageUri)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOptionsData(optionsData: OptionsData)

    @Update
    fun updateOptionsData(optionsData: OptionsData)

    fun insertImageUriList(list: List<ImageUri>){
        list.forEach {imageUri ->
            insertImageUri(imageUri)
        }
    }


    @Query("SELECT path_to_image FROM images_uri WHERE albumGroup = :albumGroup")
    fun getImagePathsFromAlbum(albumGroup: Int): List<String>


    @Query("SELECT * FROM options_data WHERE id = :id")
    fun getOptionsDataById(id: Long): OptionsData

    @Query("SELECT * FROM album WHERE name = :name")
    fun getAlbumByName(name: String): Album

    @Query("UPDATE  album SET name = :name WHERE albumGroup = :albumGroup")
    fun updateAlbumByAlbumGroup(albumGroup: Int, name: String)

    @Query("DELETE FROM album WHERE albumGroup = :albumGroup")
    fun deleteAlbumByAlbumGroup(albumGroup: Int)

    @Query("DELETE FROM images_uri WHERE id = :id")
    fun deleteImageUriById(id: Int)

    @Query("SELECT * FROM images_uri")
    fun getAllImageUri(): List<ImageUri>

    @Delete
    fun deleteImageUri(imageUri: ImageUri)

    @Update
    fun updateImageUri(imageUri: ImageUri)

    @Query("SELECT * FROM album WHERE albumGroup = :id")
    fun getAlbumById(id: Int): Album

    @Query("SELECT * FROM images_uri WHERE id = :id")
    fun getImageUriById(id: Int): ImageUri

    @Query("SELECT * FROM images_uri WHERE albumGroup = :albumGroup")
    fun getImagesFromAlbum(albumGroup: Int): List<ImageUri>

    @Query("SELECT * FROM  album")
    fun getAllAlbums(): List<Album>

    fun getAllAlbumWithImage(): MutableList<AlbumWithImages>{
        val myList = mutableListOf<AlbumWithImages>()
        getAllAlbums().forEach { album ->
            myList.add(AlbumWithImages(album, getImagesFromAlbum(album.albumGroup)))
        }
        return myList
    }

}
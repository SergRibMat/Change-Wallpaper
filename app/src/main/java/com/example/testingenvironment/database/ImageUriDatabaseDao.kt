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

    fun insertImageUriList(list: List<ImageUri>){
        list.forEach {imageUri ->
            insertImageUri(imageUri)
        }
    }

    fun DeleteAlbumAndImages(albumGroup: Int){
        //deleteAlbum(albumGroup)
        //deleteImageUriByAlbumGroup(albumGroup)
    }

    @Query("DELETE FROM ImagesUri WHERE albumGroup = :albumGroup")
    fun deleteImageUriByAlbumGroup(albumGroup: Int)

    @Query("DELETE FROM ImagesUri WHERE id = :id")
    fun deleteImageUriById(id: Int)

    @Update
    fun updateImageUri(imageUri: ImageUri)

    @Query("SELECT * FROM ImagesUri WHERE albumGroup = :albumGroup")
    fun getImagesFromAlbum(albumGroup: Int): List<Album>

    @Query("SELECT * FROM  Album")
    fun getAllAlbums(): List<Album>
}
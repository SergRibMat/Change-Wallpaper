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

    @Query("UPDATE  album SET name = :name WHERE albumGroup = :albumGroup")
    fun updateAlbumByAlbumGroup(albumGroup: Int, name: String)

    @Query("DELETE FROM album WHERE albumGroup = :albumGroup")
    fun deleteAlbumByAlbumGroup(albumGroup: Int)

    @Query("DELETE FROM ImagesUri WHERE id = :id")
    fun deleteImageUriById(id: Int)

    @Query("SELECT * FROM ImagesUri")
    fun getAllImageUri(): List<ImageUri>

    @Delete
    fun deleteImageUri(imageUri: ImageUri)

    @Update
    fun updateImageUri(imageUri: ImageUri)

    @Query("SELECT * FROM album WHERE albumGroup = :id")
    fun getAlbumById(id: Int): Album

    @Query("SELECT * FROM ImagesUri WHERE id = :id")
    fun getImageUriById(id: Int): ImageUri

    @Query("SELECT * FROM ImagesUri WHERE albumGroup = :albumGroup")
    fun getImagesFromAlbum(albumGroup: Int): List<ImageUri>

    @Query("SELECT * FROM  album")
    fun getAllAlbums(): List<Album>
}
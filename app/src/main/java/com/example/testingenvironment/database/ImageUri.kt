package com.example.testingenvironment.database

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "ImagesUri",
    /*foreignKeys = [ForeignKey(
        entity = Album::class,
        parentColumns = arrayOf("albumGroup"),
        childColumns = arrayOf("albumGroup"),
        onDelete = CASCADE
    )]*/

//tengo que darle el nombre de la variable o el que tendra en la base de datos?
)
data class ImageUri  (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "path_to_image")
    val pathToImage: String = "",
    @ColumnInfo//hacer de esta columna un foreign key
    val albumGroup: Int = 0 //0 means no group assigned
)
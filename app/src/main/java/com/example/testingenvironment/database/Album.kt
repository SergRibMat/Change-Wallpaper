package com.example.testingenvironment.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "album")
@Parcelize
data class Album (

    @ColumnInfo(name = "name")
    var name: String = "",
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "albumGroup")
    var albumGroup: Int = 0
) : Parcelable

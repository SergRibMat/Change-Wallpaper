package com.example.testingenvironment.imagealbum


import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testingenvironment.database.Album
import com.example.testingenvironment.database.AlbumWithImages

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<AlbumWithImages>?) {
    val adapter = recyclerView.adapter as ImageAlbumRecyclerViewAdapter
    adapter.submitList(data)
}

@BindingAdapter("albumName")
fun declareAlbumName(textView: TextView, album: Album){
    textView.text = album.name
}
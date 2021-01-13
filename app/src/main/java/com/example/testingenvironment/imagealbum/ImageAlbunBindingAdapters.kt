package com.example.testingenvironment.imagealbum


import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testingenvironment.MainActivity
import com.example.testingenvironment.database.Album
import com.example.testingenvironment.database.AlbumWithImages
import com.example.testingenvironment.database.ImageUri

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<AlbumWithImages>?) {

    val adapter = recyclerView.adapter as ImageAlbumRecyclerViewAdapter
    Log.i("bindRecyclerView", "Ha llegado")
    if (data != null) {
        Log.i("bindRecyclerView", "Dentro del if tambien")
        adapter.submitList(data)
    }
}

@BindingAdapter("albumName")
fun declareAlbumName(textView: TextView, album: Album){
    textView.text = album.name
}

@BindingAdapter("listImageUriData")
fun bindImageListRecyclerView(recyclerView: RecyclerView, data: List<ImageUri>?) {
        try{
            val adapter = recyclerView.adapter as ImageAlbumItemRecyclerViewAdapter

            adapter.submitList(data)

        }catch (e: Exception){

        }

}
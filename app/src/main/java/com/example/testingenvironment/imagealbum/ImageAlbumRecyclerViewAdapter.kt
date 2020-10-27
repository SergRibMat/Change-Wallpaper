package com.example.testingenvironment.imagealbum

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testingenvironment.R
import com.example.testingenvironment.database.Album
import com.example.testingenvironment.databinding.ImageAlbumItemBinding

class ImageAlbumRecyclerViewAdapter(val clickListener: AlbumListener) : ListAdapter<Album, ItemViewHolder>(DiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ImageAlbumItemBinding.inflate(LayoutInflater.from(parent.context)))
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    companion object DiffCallback :
        DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.albumGroup == newItem.albumGroup
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem == newItem
        }
    }

}

class ItemViewHolder constructor(val binding: ImageAlbumItemBinding) : RecyclerView.ViewHolder(binding.root) {


    fun bind(album: Album, clickListener: AlbumListener) {
        binding.album = album
        //binding.album = Album("Sergio", 1)
        binding.clickListener = clickListener
        binding.executePendingBindings()//use always this line when using bindings and recyclerViews
    }

}

class AlbumListener(val clickListener: (albumGroup: Int) -> Unit) {
    fun onClick(album: Album) = clickListener(album.albumGroup)
}

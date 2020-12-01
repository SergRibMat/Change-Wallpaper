package com.example.testingenvironment.imagealbum

import android.util.Log
import android.view.*
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
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem == newItem
        }
    }

}

class ItemViewHolder constructor(val binding: ImageAlbumItemBinding) : RecyclerView.ViewHolder(binding.root), View.OnCreateContextMenuListener {


    fun bind(album: Album, clickListener: AlbumListener) {
        binding.album = album
        //binding.album = Album("Sergio", 1)
        //registerForContextMenu(binding.albumItemLinearLayout)
        binding.clickListener = clickListener
        binding.albumItemLinearLayout.setOnCreateContextMenuListener(this)
        binding.executePendingBindings()//use always this line when using bindings and recyclerViews
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menu?.add(adapterPosition, 0, 0, "Delete")
        menu?.add(adapterPosition, 1, 1, "Rename")
    }

}

class AlbumListener(val clickListener: (album: Album) -> Unit) {
    fun onClick(album: Album) = clickListener(album)
}

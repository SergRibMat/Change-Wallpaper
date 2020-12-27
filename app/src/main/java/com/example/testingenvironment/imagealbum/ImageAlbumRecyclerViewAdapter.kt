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
import com.example.testingenvironment.database.AlbumWithImages
import com.example.testingenvironment.databinding.ImageAlbumItemBinding

class ImageAlbumRecyclerViewAdapter(val clickListener: AlbumListener) : ListAdapter<AlbumWithImages, ItemViewHolder>(DiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ImageAlbumItemBinding.inflate(LayoutInflater.from(parent.context)))
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    companion object DiffCallback :
        DiffUtil.ItemCallback<AlbumWithImages>() {
        override fun areItemsTheSame(oldItem: AlbumWithImages, newItem: AlbumWithImages): Boolean {
            return oldItem.album.name == newItem.album.name
        }

        override fun areContentsTheSame(oldItem: AlbumWithImages, newItem: AlbumWithImages): Boolean {
            return /*oldItem.album == newItem.album*/ false
        }
    }

}

class ItemViewHolder constructor(val binding: ImageAlbumItemBinding) : RecyclerView.ViewHolder(binding.root), View.OnCreateContextMenuListener {


    fun bind(albumWithImages: AlbumWithImages, clickListener: AlbumListener) {
        binding.albumWithImages = albumWithImages
        binding.clickListener = clickListener

        binding.albumItemLinearLayout.setOnCreateContextMenuListener(this)
        binding.executePendingBindings()//use always this line when using bindings and recyclerViews
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        val order: Int? = binding.albumWithImages?.album?.albumGroup ?: 0
        val order2: Int = order!!
        menu?.add(adapterPosition, 0, order2, "Delete")
        menu?.add(adapterPosition, 1, order2 + 1, "Rename")
    }


}

class AlbumListener(val clickListener: (album: Album) -> Unit) {
    fun onClick(album: Album) = clickListener(album)
}

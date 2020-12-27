package com.example.testingenvironment.imagealbum

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testingenvironment.database.ImageUri
import com.example.testingenvironment.databinding.ImageItemAlbumListBinding


class ImageAlbumItemRecyclerViewAdapter: ListAdapter<ImageUri, ImageAlbumItemRecyclerViewAdapter.ImageUriViewHolder>(DiffCallback) {
    companion object DiffCallback : DiffUtil.ItemCallback<ImageUri/*here goes the type of object you want to compare*/>() {
        override fun areItemsTheSame(oldItem: ImageUri, newItem: ImageUri): Boolean {
            return oldItem === newItem //compares if the objects are the same
        }

        override fun areContentsTheSame(oldItem: ImageUri, newItem: ImageUri): Boolean {
            return oldItem.id == newItem.id //compares if this property of the objects are the same
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageAlbumItemRecyclerViewAdapter.ImageUriViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ImageAlbumItemRecyclerViewAdapter.ImageUriViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    class ImageUriViewHolder(private var binding: ImageItemAlbumListBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUri: ImageUri) {
            binding.executePendingBindings()
        }
    }
}
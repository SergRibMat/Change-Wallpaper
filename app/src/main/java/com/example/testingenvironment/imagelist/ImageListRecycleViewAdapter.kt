package com.example.testingenvironment.imagelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testingenvironment.database.Album
import com.example.testingenvironment.database.ImageUri
import com.example.testingenvironment.databinding.ImageListItemBinding

class ImageListRecycleViewAdapter(val clickListener: ImageUriClickListener): ListAdapter<ImageUri, ImageListRecycleViewAdapter.ImageUriViewHolder>(DiffCallback) {
    class ImageUriViewHolder(private var binding: ImageListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(imageUri: ImageUri, clickListener: ImageUriClickListener){
            binding.imageUri = imageUri
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

    //this diffUtil class only works if the objects are the same, if they are different object, it wont compare the content
    companion object DiffCallback : DiffUtil.ItemCallback<ImageUri/*here goes the type of object you want to compare*/>() {
        override fun areItemsTheSame(oldItem: ImageUri, newItem: ImageUri): Boolean {
            return oldItem === newItem //compares if the objects are the same
        }

        override fun areContentsTheSame(oldItem: ImageUri, newItem: ImageUri): Boolean {
            return oldItem.name == newItem.name //compares if this property of the objects are the same
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageListRecycleViewAdapter.ImageUriViewHolder {
        return ImageUriViewHolder(ImageListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ImageListRecycleViewAdapter.ImageUriViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }
}

class ImageUriClickListener(val clickListener: (imageUri: ImageUri) -> Unit) {
    fun onClick(imageUri: ImageUri) = clickListener(imageUri)
}

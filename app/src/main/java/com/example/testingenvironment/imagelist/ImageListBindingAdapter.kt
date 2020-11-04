package com.example.testingenvironment.imagelist

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.testingenvironment.R
import com.example.testingenvironment.database.ImageUri


@BindingAdapter("imageUriListData")//listData will be transformed into the name property that this will have
fun bindRecyclerView(recyclerView: RecyclerView, data: List<ImageUri>?) {
    val adapter = recyclerView.adapter as ImageListRecycleViewAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageUri")
fun drawImageIntoImageView(imageView: ImageView, pathToFile: String){
        Glide.with(imageView.context)
            .load(pathToFile)
            .override(imageView.width, imageView.height)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imageView)
}


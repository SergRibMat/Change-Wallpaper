package com.example.testingenvironment.imagelist

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testingenvironment.database.ImageUri

@BindingAdapter("imageUriListData")//listData will be transformed into the name property that this will have
fun bindRecyclerView(recyclerView: RecyclerView, data: List<ImageUri>?) {
    val adapter = recyclerView.adapter as ImageListRecycleViewAdapter
    adapter.submitList(data)
}



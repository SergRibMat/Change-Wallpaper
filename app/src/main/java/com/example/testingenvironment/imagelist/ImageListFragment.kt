package com.example.testingenvironment.imagelist

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.testingenvironment.R
import com.example.testingenvironment.databinding.DetailFragmentBinding
import com.example.testingenvironment.databinding.ImageAlbumFragmentBinding
import com.example.testingenvironment.databinding.ImageListFragmentBinding

lateinit var binding: ImageListFragmentBinding

class ImageListFragment : Fragment() {



    private lateinit var viewModel: ImageListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.image_list_fragment, container, false)

        return inflater.inflate(R.layout.image_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ImageListViewModel::class.java)
        // TODO: Use the ViewModel
    }

    fun toDetailListener(){

    }

}
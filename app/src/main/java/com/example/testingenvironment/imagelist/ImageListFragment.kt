package com.example.testingenvironment.imagelist

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.testingenvironment.database.ImageUriDatabase
import com.example.testingenvironment.databinding.ImageListFragmentBinding



class ImageListFragment : Fragment() {


    private lateinit var viewModel: ImageListViewModel
    lateinit var binding: ImageListFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ImageListFragmentBinding.inflate(inflater)

        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val application = requireNotNull(this.activity).application

        //reference to the data sourse
        val dataSource = ImageUriDatabase.getInstance(application).imageUriDatabaseDao

        val viewModelFactory = ImageListViewModelFactory(dataSource, application, setAlbumGroupIntoViewModel())

        viewModel = ViewModelProvider(this, viewModelFactory).get(ImageListViewModel::class.java)

        binding.viewModel = viewModel

        binding.imageListGrid.adapter = ImageListRecycleViewAdapter(ImageUriClickListener { imageUri ->
            viewModel.navigateToDetailFragment(imageUri)
        })

        declareObservers()
    }

    fun setAlbumGroupIntoViewModel(): Int{
        val args = ImageListFragmentArgs.fromBundle(requireArguments())
        return args.album.albumGroup
    }

    private fun declareObservers() {
        viewModel.navigateToDetail.observe(viewLifecycleOwner, {
            if ( null != it ) {
                this.findNavController().navigate(ImageListFragmentDirections.actionImageListFragmentToDetailFragment(it))
                viewModel.navigateToDetailFragmentCompleted()
            }
        })
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    private fun showToast(text: Int) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }



}
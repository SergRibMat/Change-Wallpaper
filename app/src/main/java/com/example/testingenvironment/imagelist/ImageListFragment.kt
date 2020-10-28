package com.example.testingenvironment.imagelist

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.testingenvironment.R
import com.example.testingenvironment.database.ImageUri
import com.example.testingenvironment.databinding.DetailFragmentBinding
import com.example.testingenvironment.databinding.ImageAlbumFragmentBinding
import com.example.testingenvironment.databinding.ImageListFragmentBinding
import com.example.testingenvironment.imagealbum.REQUEST_IMAGE_GET

lateinit var binding: ImageListFragmentBinding

class ImageListFragment : Fragment() {



    private lateinit var viewModel: ImageListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ImageListFragmentBinding.inflate(inflater)


        val args = ImageListFragmentArgs.fromBundle(requireArguments())

        setButtonListeners()
        toDetailListener()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ImageListViewModel::class.java)

        declareObservers()

        binding.viewModel = viewModel

        binding.imageListGrid.adapter = ImageListRecycleViewAdapter(ImageUriClickListener { imageUri ->
            viewModel.navigateToDetailFragment(imageUri)
        })
    }







    private fun setButtonListeners() {
        binding.addImagesBtn.setOnClickListener {
            selectImage()
        }
    }

    private fun declareObservers() {
        viewModel.navigateToDetail.observe(viewLifecycleOwner, {
            if ( null != it ) {
                this.findNavController().navigate(ImageListFragmentDirections.actionImageListFragmentToDetailFragment(it))
                viewModel.navigateToDetailFragmentCompleted()
            }
        })
    }

    fun toDetailListener(){
        binding.navToDetailBtn.setOnClickListener {
            //viewModel.navigateToDetailFragment(ImageUri(1, "Sergio", "path", 4))
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun showToast(text: Int) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun oneOrMultiple(data: Intent?){
        if (data != null){
            var data: ClipData = data.clipData!!
            fromClipDataToList(data)
            //viewModel.imageUriList.value = imageList.toList()
        }
    }

    private fun fromClipDataToList(data: ClipData): List<String> {
        val imageList = mutableListOf<String>()
        for (i in 0 until data.itemCount) {
            var item = data.getItemAt(i).uri
            imageList.add(i, item.toString())
            //printImageWithGlide(this, carro , this.findViewById<ImageView>(R.id.show_picture_imageview))
        }
        return imageList.toList()
    }

    private fun createIntent(action: String, typeString: String): Intent {
        return Intent(action).apply {
            type = typeString
        }
    }


    private fun selectImage() {
        val intent = createIntent(Intent.ACTION_GET_CONTENT, "image/*")
            .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        //if (intent.resolveActivity(packageManager) != null) {
        startActivityForResult(intent, REQUEST_IMAGE_GET)
        //}
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            oneOrMultiple(data)
        }
    }

}
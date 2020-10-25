package com.example.testingenvironment.imagealbum

import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.testingenvironment.R
import com.example.testingenvironment.databinding.DetailFragmentBinding
import com.example.testingenvironment.databinding.ImageAlbumFragmentBinding
import com.example.testingenvironment.databinding.ImageListFragmentBinding


const val REQUEST_IMAGE_GET = 101

class ImageAlbumFragment : Fragment() {

    lateinit var binding: ImageAlbumFragmentBinding
    private lateinit var viewModel: ImageAlbumViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate<ImageAlbumFragmentBinding>(inflater, R.layout.image_album_fragment, container, false)

        setGetImageButtonListener()
        buttonNavToImageList()

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ImageAlbumViewModel::class.java)




        // TODO: Use the ViewModel
    }

    fun setGetImageButtonListener() {
        binding.getImageButton.setOnClickListener {view ->
            selectImage()

        }
    }

    fun buttonNavToImageList(){
        binding.toImageListButton.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_imageAlbumFragment_to_imageListFragment)
        }
    }

    fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun createIntent(action: String, typeString: String): Intent {
        return Intent(action).apply {
            type = typeString
        }
    }


    fun selectImage() {
        var intent = createIntent(Intent.ACTION_GET_CONTENT, "image/*")
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

    fun printImageWithGlide(context: Context, imgUri: Uri?, imgView: ImageView) {
        Glide.with(context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
            )
            .into(imgView)
    }

    fun oneOrMultiple(data: Intent?){
        if (data != null){
            var data: ClipData = data.clipData!!
            val imageList = mutableListOf<Uri>()
            for (i in 0 until data.itemCount){
                var item = data.getItemAt(i).uri
                imageList.add(i, item)
                //printImageWithGlide(this, carro , this.findViewById<ImageView>(R.id.show_picture_imageview))
            }
            viewModel.imageUriList.value = imageList
        }
    }

    fun setUpObservers(){
        viewModel.imageUriList.observe(this, Observer { imageUriList ->
            //give list to the database
        })
    }

}
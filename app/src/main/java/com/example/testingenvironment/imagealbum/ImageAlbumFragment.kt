package com.example.testingenvironment.imagealbum

import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.testingenvironment.R
import com.example.testingenvironment.database.ImageUriDatabase
import com.example.testingenvironment.databinding.ImageAlbumFragmentBinding


const val REQUEST_IMAGE_GET = 101

class ImageAlbumFragment : Fragment() {

    lateinit var binding: ImageAlbumFragmentBinding
    private lateinit var viewModel: ImageAlbumViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ImageAlbumFragmentBinding.inflate(inflater)

        setOnClickListener()
        buttonNavToImageList()

        binding.lifecycleOwner = this

        //require not null is a kotlin function that throws an illegal argument exception if the value is null

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val application = requireNotNull(this.activity).application

        //reference to the data sourse
        val dataSource = ImageUriDatabase.getInstance(application).imageUriDatabaseDao

        val viewModelFactory = ImageAlbumViewModelFactory(dataSource, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(ImageAlbumViewModel::class.java)

        binding.viewModel = viewModel

        binding.albumList.adapter = ImageAlbumRecyclerViewAdapter()

        //showToast("Hay ${viewModel.albumList.value?.size} elementos en la lista")
    }

    private fun setOnClickListener() {
        binding.getImageButton.setOnClickListener { view ->
            selectImage()
        }

        binding.fab.setOnClickListener { view ->
            createNewAlbumAlertDialog()
            viewModel.loadAlbumsIntoList()
            showToast("Hay ${viewModel.albumList.value?.size} elementos en la lista")
        }
    }

    private fun buttonNavToImageList(){
        binding.toImageListButton.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_imageAlbumFragment_to_imageListFragment)
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun showToast(text: Int) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
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
            val imageList = mutableListOf<String>()
            for (i in 0 until data.itemCount){
                var item = data.getItemAt(i).uri
                imageList.add(i, item.toString())
                //printImageWithGlide(this, carro , this.findViewById<ImageView>(R.id.show_picture_imageview))
            }
            viewModel.imageUriList.value = imageList.toList()
        }
    }

    private fun createNewAlbumAlertDialog(){
        val etFolder = createFolderEditText()
        if (etFolder.parent != null) {
            (etFolder.parent as ViewGroup).removeView(etFolder) // esto soluciona el problema java.lang.IllegalStateException: The specified child already has a parent. You must call removeView() on the child's parent first.
        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
        builder.setTitle(getString(R.string.newgroup))
        builder.setMessage(getString(R.string.entername))
        builder.setView(etFolder)
        builder.setNegativeButton(
            getString(R.string.cancel)
        ) { dialogInterface, i ->
            showToast(R.string.actioncanceled)
            dialogInterface.cancel()
        }.setPositiveButton(
            getString(R.string.accept)
        ) { dialogInterface, i ->
            val name = etFolder.text.toString().trim { it <= ' ' }
            viewModel.saveAlbumIntoDatabase(name)
            dialogInterface.cancel()
        }

        builder.show()
    }

    private fun createFolderEditText(): EditText {
        val myEditText = EditText(context) // Pass it an Activity or Context
        myEditText.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        ) // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
        return myEditText
    }

}

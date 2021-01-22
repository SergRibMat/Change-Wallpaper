package com.example.testingenvironment.imagealbum

import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.testingenvironment.R
import com.example.testingenvironment.database.ImageUriDatabase
import com.example.testingenvironment.databinding.ImageAlbumFragmentBinding
import com.example.testingenvironment.databinding.ImageAlbumItemBinding
import kotlinx.coroutines.*

const val REQUEST_IMAGE_GET = 101


class ImageAlbumFragment : Fragment() {

    lateinit var binding: ImageAlbumFragmentBinding
    private lateinit var viewModel: ImageAlbumViewModel
    private lateinit var imageAlbumItemBinding: ImageAlbumItemBinding

    //coroutine
    private var getImagesJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main +  getImagesJob)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ImageAlbumFragmentBinding.inflate(inflater)

        imageAlbumItemBinding = ImageAlbumItemBinding.inflate(inflater)

        setOnClickListener()

        binding.lifecycleOwner = this
        imageAlbumItemBinding.lifecycleOwner = this

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

        setUpObservers()

        loadAdapter()

        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!,
            view!!.findNavController())
                || super.onOptionsItemSelected(item)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.overflow_menu, menu)

    }

    fun loadAdapter(){

        binding.albumList.adapter = ImageAlbumRecyclerViewAdapter(
            AlbumListener { album ->
                viewModel.navigateToImageListFragment(album)
            },
            AlbumListener { album ->
                viewModel.albumGroup = album.albumGroup
                selectImage()
            }
        )
    }

    private fun setOnClickListener() {
        binding.addAlbumBtn.setOnClickListener { view ->
            createNewAlbumAlertDialog()
            /*viewModel.albumWithImageList.observe(viewLifecycleOwner, {
                showToast("It changed")
            })*/
            //val listSize = viewModel.albumWithImageList.value?.size
            //val albumListSize = viewModel.albumList.value?.size
            //showToast("$listSize")
        }
    }

    fun setUpObservers() {
        viewModel.navigateToImageList.observe(viewLifecycleOwner, {
            if (null != it) {
                this.findNavController()
                    .navigate(ImageAlbumFragmentDirections.actionImageAlbumFragmentToImageListFragment(it))
                viewModel.navigateToImageListFragmentCompleted()
            }
        })
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
    private fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun showToast(text: Int) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun createNewAlbumAlertDialog() {
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
            if (name.isNotEmpty()) {
                if (!viewModel.albumAlreadyExists(name)){
                    uiScope.launch {
                        viewModel.saveAlbumIntoDatabase(name)
                        viewModel.loadAlbumWithImagesIntoList()
                        loadAdapter()
                    }
                }else{
                    showToast("Album already exists")
                }

            }
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

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            0 -> {//delete
                val id = item.order
                viewModel.deleteAlbum(id)
                true
            }
            1 -> {//rename
                val id = item.order - 1
                renameAlbumAlertDialog(id)

                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun renameAlbumAlertDialog(id: Int) {
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
            if (name.isNotEmpty()) {
                viewModel.updateAlbumById(id, name)
            }
            dialogInterface.cancel()
        }

        builder.show()
    }

    fun oneOrMultiple(data: Intent?) {
        var dataAsList: ClipData? = data?.clipData

        if (dataAsList != null) {
            viewModel.insertImagesIntoDatabase(fromClipDataToList(dataAsList))
        } else {
            if (data != null) {
                var data: String? = data.dataString
                viewModel.insertImagesIntoDatabase(listOf(data!!))
            }

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
        val intent = createIntent(Intent.ACTION_OPEN_DOCUMENT, "image/*")
            .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        //if (intent.resolveActivity(packageManager) != null) {
        startActivityForResult(intent, REQUEST_IMAGE_GET)
        //}
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            uiScope.launch {
                binding.loadingPb.visibility = VISIBLE
                oneOrMultiple(data)
                loadAdapter()
                binding.loadingPb.visibility = GONE
            }
        }

    }

}

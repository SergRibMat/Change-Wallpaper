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


const val REQUEST_IMAGE_GET = 101

class ImageListFragment : Fragment() {


    private lateinit var viewModel: ImageListViewModel
    lateinit var binding: ImageListFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ImageListFragmentBinding.inflate(inflater)


        binding.lifecycleOwner = this

        setButtonListeners()


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val application = requireNotNull(this.activity).application

        //reference to the data sourse
        val dataSource = ImageUriDatabase.getInstance(application).imageUriDatabaseDao

        val viewModelFactory = ImageListViewModelFactory(dataSource, application, setAlbumGroupIntoViewModel())

        viewModel = ViewModelProvider(this, viewModelFactory).get(ImageListViewModel::class.java)

        toDetailListener()

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

        }
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    private fun showToast(text: Int) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    fun oneOrMultiple(data: Intent?){
        //there is no option. I need to save the images in the app and take that
        var dataAsList: ClipData? = data?.clipData

        if (dataAsList != null){
                viewModel.insertImagesIntoDatabase(fromClipDataToList(dataAsList))
            }else{
            if(data != null){
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
            oneOrMultiple(data)
        }
    }
}
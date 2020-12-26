package com.example.testingenvironment.imagealbum

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.testingenvironment.R
import com.example.testingenvironment.database.ImageUriDatabase
import com.example.testingenvironment.databinding.ImageAlbumFragmentBinding




class ImageAlbumFragment : Fragment() {

    lateinit var binding: ImageAlbumFragmentBinding
    private lateinit var viewModel: ImageAlbumViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ImageAlbumFragmentBinding.inflate(inflater)

        setOnClickListener()

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

        setUpObservers()

        binding.albumList.adapter = ImageAlbumRecyclerViewAdapter(AlbumListener { album ->
            viewModel.navigateToImageListFragment(album)
        })
    }

    private fun setOnClickListener() {
        binding.addAlbumBtn.setOnClickListener { view ->
            //createNewAlbumAlertDialog()
            //viewModel.loadAlbumsIntoList()
            val listSize = viewModel.albumWithImageList.value?.size
            val albumListSize = viewModel.albumList.value?.size
            showToast("$listSize")
        }
    }

    fun setUpObservers(){
        viewModel.navigateToImageList.observe(viewLifecycleOwner, {
            if ( null != it ) {
                this.findNavController().navigate(ImageAlbumFragmentDirections.actionImageAlbumFragmentToImageListFragment(it))
                viewModel.navigateToImageListFragmentCompleted()
            }
        })
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun showToast(text: Int) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
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
            if (name.isNotEmpty()){
                viewModel.saveAlbumIntoDatabase(name)
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

    private fun renameAlbumAlertDialog(id: Int){
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
            if (name.isNotEmpty()){
                viewModel.updateAlbumById(id, name)
            }
            dialogInterface.cancel()
        }

        builder.show()
    }

}

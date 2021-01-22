package com.example.testingenvironment.detail

import android.app.WallpaperManager
import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.testingenvironment.R
import com.example.testingenvironment.databinding.DetailFragmentBinding
import com.example.testingenvironment.databinding.ImageAlbumFragmentBinding
import com.example.testingenvironment.imagelist.ImageListFragmentArgs
import com.example.testingenvironment.imagelist.ImageUriClickListener
import kotlinx.coroutines.*

class DetailFragment : Fragment() {

    lateinit var binding: DetailFragmentBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DetailFragmentBinding.inflate(inflater)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)



        val args = DetailFragmentArgs.fromBundle(requireArguments())
        binding.imageUri = args.ImageUri
        binding.clickListener = ImageUriClickListener {
            setWallpaperMethod(it.pathToImage, this.context!!)
        }

    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun showToast(text: Int) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun setWallpaperMethod(stringPathToImage: String, appContext: Context) {//call this method on a background thread

        var activityJob = Job()
        val uiScope = CoroutineScope(Dispatchers.IO + activityJob)

        if (!stringPathToImage.isNullOrEmpty()) {
            uiScope.launch {
                val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(appContext)
                if (wallpaperManager.isSetWallpaperAllowed) {
                wallpaperManager.setBitmap(getImageWithGlide(stringPathToImage, appContext))
                }
                withContext(Dispatchers.Main){
                    showToast("Wallpaper changed")
                }
                activityJob.cancel()

            }
        }

    }

    private fun getImageWithGlide(stringPathToImage: String, appContext: Context) =
        Glide.with(appContext)
            .asBitmap()
            .load(stringPathToImage)
            .submit()
            .get()

}
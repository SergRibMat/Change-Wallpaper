package com.example.testingenvironment.options

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.testingenvironment.R
import com.example.testingenvironment.databinding.ImageAlbumFragmentBinding
import com.example.testingenvironment.databinding.OptionsFragmentBinding

class OptionsFragment : Fragment() {


    private lateinit var viewModel: OptionsViewModel
    lateinit var binding: OptionsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = OptionsFragmentBinding.inflate(inflater)

        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OptionsViewModel::class.java)


        if (binding.activateSetWallpaperSwitch.isChecked){
            showToast("YES")
        }else{
            showToast("NO")
        }

        binding.activateSetWallpaperSwitch.isChecked = true

        val adapter: ArrayAdapter<String> = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, stringList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.albumListSpinner.adapter = adapter


    }

    fun stringList(): List<String> = mutableListOf(
        "String 1",
        "String 2",
        "String 3"
    )

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
    private fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun showToast(text: Int) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

}
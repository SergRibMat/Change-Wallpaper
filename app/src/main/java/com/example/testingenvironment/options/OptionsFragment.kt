package com.example.testingenvironment.options

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.example.testingenvironment.MainActivity
import com.example.testingenvironment.database.ImageUriDatabase
import com.example.testingenvironment.database.OptionsData
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

        val application = requireNotNull(this.activity).application

        //reference to the data sourse
        val dataSource = ImageUriDatabase.getInstance(application).imageUriDatabaseDao

        val viewModelFactory = OptionsViewModelFactory(dataSource)

        viewModel = ViewModelProvider(this, viewModelFactory).get(OptionsViewModel::class.java)

        viewModel.album.observe(viewLifecycleOwner, { album ->
            viewModel.assigPeriodicWorkRequestToLiveData()
            Log.i("OptionsFragment.", "assigPeriodicWorkRequestToLiveData()")
        })

        setDefaultValuesToUI()

        createSpinnerWithAdapter()

        createSwitchListener()

        createSpinnerListener()

    }

    fun createSwitchListener(){
        binding.activateSetWallpaperSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                val albumName = viewModel.optionsData.value!!.selectedAlbum

                if(albumName != null && albumName != "Empty"){

                        scheduleWorker()



                }else{
                    showToast("You need to select an album")
                    binding.activateSetWallpaperSwitch.isChecked = false
                }
            }else{
                WorkManager.getInstance().cancelUniqueWork(MainActivity.WORKER_NAME)//este funciona
                showToast("Change Wallpaper Process is OFF")
            }

            viewModel.optionsData.value!!.isSelected = binding.activateSetWallpaperSwitch.isChecked
        }
    }

    fun createSpinnerListener(){
        binding.albumListSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val albumSelected = parent?.selectedItem.toString().trim()
                viewModel.getAlbumByName(albumSelected)//this method fills the livedata
                viewModel.optionsData.value!!.selectedAlbum = albumSelected

                //when you select another albun, forces switch button to OFF so the user needs to ON and execute code
                binding.activateSetWallpaperSwitch.isChecked = false
                viewModel.optionsData.value!!.isSelected = binding.activateSetWallpaperSwitch.isChecked
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    fun createSpinnerWithAdapter(){
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_item,
            viewModel.albumNameList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.albumListSpinner.adapter = adapter

        declareObserverRefreshAdapter(adapter)

    }

    private fun setDbOptionAtPositionInSpinner(optionsData: OptionsData) {

        val snipperAdapter = binding.albumListSpinner.adapter as ArrayAdapter<String>
        val itemPosition = snipperAdapter.getPosition(optionsData.selectedAlbum)
        binding.albumListSpinner.setSelection(itemPosition, true)
    }

    fun declareObserverRefreshAdapter(adapter: ArrayAdapter<String> ){
        viewModel.albumList.observe(viewLifecycleOwner, {
            adapter.addAll(viewModel.albumNameList())
        })
    }

    fun scheduleWorker(){
        if(viewModel.periodicWorkRequest.value != null){
            WorkManager.getInstance().enqueueUniquePeriodicWork(
                MainActivity.WORKER_NAME,//just the reference to the variable in the companion object
                ExistingPeriodicWorkPolicy.KEEP,//what to do when there are 2 request enqueued of the same work
                viewModel.periodicWorkRequest.value!!)
            showToast("Change Wallpaper Process is ON")
        }

    }

    private fun setDefaultValuesToUI(){

        //default value for snipper is in createSpinnerWithAdapter()
        viewModel.optionsData.observe(viewLifecycleOwner, { optionsData ->
            binding.activateSetWallpaperSwitch.isChecked = optionsData.isSelected
            setDbOptionAtPositionInSpinner(optionsData)
        })

        //set radio buttons default value
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

    override fun onStop() {
        super.onStop()
        viewModel.updateOptionsDataDatabase()
    }



}
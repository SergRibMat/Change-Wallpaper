package com.example.testingenvironment.options

import android.content.Context
import android.os.Build
import android.os.Bundle
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
import com.example.testingenvironment.database.ImageUri
import com.example.testingenvironment.database.ImageUriDatabase
import com.example.testingenvironment.databinding.OptionsFragmentBinding
import com.example.testingenvironment.worker.SetWallpaperWorker
import java.util.concurrent.TimeUnit

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


        binding.activateSetWallpaperSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            // do something, the isChecked will be
            // true if the switch is in the On position
            showToast("$isChecked")
            if (isChecked){
                scheduleWorker()
            }else{
                WorkManager.getInstance().cancelAllWorkByTag(MainActivity.WORKER_NAME)
            }
        }


        val adapter: ArrayAdapter<String> = ArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_item,
            viewModel.albumNameList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.albumListSpinner.adapter = adapter

        viewModel.albumList.observe(viewLifecycleOwner, {
            adapter.addAll(viewModel.albumNameList())
        })

        binding.albumListSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                showToast("${parent?.selectedItem.toString()}")
                //null text control for first time
                viewModel.getImagesFromAlbum(parent?.selectedItem.toString())//this method fills the livedata
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                showToast("nothing was selected")
            }
        }

        viewModel.selectedImagesList.observe(viewLifecycleOwner, {
            //create worker class?
            viewModel.assigPeriodicWorkRequestToLiveData()
        })

    }

    fun scheduleWorker(){
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            MainActivity.WORKER_NAME,//just the reference to the variable in the companion object
            ExistingPeriodicWorkPolicy.KEEP,//what to do when there are 2 request enqueued of the same work
            viewModel.periodicWorkRequest.value!!)
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

}
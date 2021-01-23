package sergio.ribera.testingenvironment.options

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
import sergio.ribera.testingenvironment.MainActivity
import sergio.ribera.testingenvironment.database.ImageUriDatabase
import sergio.ribera.testingenvironment.database.OptionsData
import sergio.ribera.testingenvironment.databinding.OptionsFragmentBinding


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
        })

        setDefaultValuesToUI()

        createSpinnerWithAdapter()

    }

    fun createRadioGroupListener(){

        binding.timeRd.setOnCheckedChangeListener { buttonView, checkedRbId ->
            viewModel.setTimeAndCurrencyToOptionsData(checkedRadioButton(checkedRbId))
        }
    }

    /*
    * 0 -> Minutes
    * 1 -> Days
    * 2 -> Hours*/
    fun checkedRadioButton(checkedRadioButtonId: Int): TimeAndCurrency = when(checkedRadioButtonId){
        binding.radioButton1.id -> TimeAndCurrency(45L, 0)
        binding.radioButton2.id -> TimeAndCurrency(2L, 2)
        binding.radioButton3.id -> TimeAndCurrency(4L, 2)
        binding.radioButton4.id -> TimeAndCurrency(8L, 2)
        binding.radioButton5.id -> TimeAndCurrency(12L, 2)
        binding.radioButton6.id -> TimeAndCurrency(1L, 1)
        else -> TimeAndCurrency(30L, 0)

    }

    fun setDefaultTimeRadioGroup(time: Long) = when(time){
        45L -> binding.radioButton1.isChecked = true
        2L -> binding.radioButton2.isChecked = true
        4L -> binding.radioButton3.isChecked = true
        8L -> binding.radioButton4.isChecked = true
        12L -> binding.radioButton5.isChecked = true
        1L -> binding.radioButton6.isChecked = true
        else -> binding.radioButton1.isChecked = true
    }

    fun createSwitchListener(){
        binding.activateSetWallpaperSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                val albumName = viewModel.optionsData.value!!.selectedAlbum

                if(albumName != null && albumName != "Empty"){

                        scheduleWorker()

                        enableOptionViews(false)
                    showToast("Change Wallpaper Process is ON")

                }else{
                    showToast("You need to select an album")
                    binding.activateSetWallpaperSwitch.isChecked = false
                }
            }else{
                WorkManager.getInstance().cancelUniqueWork(MainActivity.WORKER_NAME)//este funciona
                showToast("Change Wallpaper Process is OFF")
                enableOptionViews(true)
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

    fun declareObserverRefreshAdapter(adapter: ArrayAdapter<String>){
        viewModel.albumList.observe(viewLifecycleOwner, {
            adapter.addAll(viewModel.albumNameList())
        })
    }

    fun scheduleWorker(){
        if(viewModel.periodicWorkRequest.value != null){
            WorkManager.getInstance().enqueueUniquePeriodicWork(
                MainActivity.WORKER_NAME,//just the reference to the variable in the companion object
                ExistingPeriodicWorkPolicy.KEEP,//what to do when there are 2 request enqueued of the same work
                viewModel.periodicWorkRequest.value!!
            )
            showToast("Change Wallpaper Process is ON")
        }

    }

    private fun setDefaultValuesToUI(){

        //default value for snipper is in createSpinnerWithAdapter()
        viewModel.optionsData.observe(viewLifecycleOwner, { optionsData ->
            binding.activateSetWallpaperSwitch.isChecked = optionsData.isSelected
            setDbOptionAtPositionInSpinner(optionsData)
            setDefaultTimeRadioGroup(optionsData.time)
            enableOptionViews(!optionsData.isSelected)
            createSwitchListener()

            createSpinnerListener()

            createRadioGroupListener()

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

    fun enableOptionViews(condition: Boolean){
        binding.albumListSpinner.isEnabled = condition
        binding.timeRd.isEnabled = condition
        binding.radioButton1.isEnabled = condition
        binding.radioButton2.isEnabled = condition
        binding.radioButton3.isEnabled = condition
        binding.radioButton4.isEnabled = condition
        binding.radioButton5.isEnabled = condition
        binding.radioButton6.isEnabled = condition

    }

}
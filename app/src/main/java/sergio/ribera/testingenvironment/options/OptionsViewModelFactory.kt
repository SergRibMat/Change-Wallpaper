package sergio.ribera.testingenvironment.options

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sergio.ribera.testingenvironment.database.ImageUriDatabaseDao

class OptionsViewModelFactory(
    private val dataSource: ImageUriDatabaseDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OptionsViewModel::class.java)) {
            return OptionsViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
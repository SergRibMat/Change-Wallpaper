package sergio.ribera.testingenvironment

import android.Manifest
import android.content.Context

import android.content.DialogInterface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener


class MainActivity : AppCompatActivity() {


    companion object {
        private var instance: MainActivity? = null
        const val WORKER_NAME: String = "CHANGE_WALLPAPER"

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        instance = this

        val navController = this.findNavController(R.id.myNavHostFragment)

        NavigationUI.setupActionBarWithNavController(this, navController)

        //Can i dictate from here which is the first Fragment i want to show? Can it follow navigation?


    }



    override fun onSupportNavigateUp(): Boolean {

        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, null)
    }

    fun withDexter() {

        Dexter.withContext(this)
            .withPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    showToast("Permission granted")
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    showToast("Permission denied")
                }

                override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, token: PermissionToken?) {
                    showToast("onPermissionRationaleShouldBeShown")

                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("Give Permission Internet")
                        .setMessage("You need to give permission internet for this app")
                        .setNegativeButton(
                            android.R.string.cancel,
                            DialogInterface.OnClickListener { dialogInterface, i ->
                                dialogInterface.dismiss()
                                token?.cancelPermissionRequest()
                            })
                        .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialogInterface, i ->
                            dialogInterface.dismiss()
                            token?.continuePermissionRequest()
                        })
                        .show()
                }

            })
            .check()


    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun showToast(text: Int) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    fun appPermissionList(): List<String> = mutableListOf(

    )

    override fun onDestroy() {
        super.onDestroy()
        //init background permanent Service
    }



}






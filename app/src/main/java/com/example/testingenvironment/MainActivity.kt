package com.example.testingenvironment

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


const val REQUEST_IMAGE_GET = 101


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setGetImageButtonListener()
    }

    fun setGetImageButtonListener() {
        this.findViewById<Button>(R.id.get_image_button).setOnClickListener {
            selectImage()
        }
    }

    fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    fun createIntent(action: String, typeString: String): Intent {
        return Intent(action).apply {
            type = typeString
        }
    }


    fun selectImage() {
        intent = createIntent(Intent.ACTION_GET_CONTENT, "image/*")
            .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        //if (intent.resolveActivity(packageManager) != null) {
        startActivityForResult(intent, REQUEST_IMAGE_GET)
        //}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            oneOrMultiple(data)
        }
    }

    fun printImageWithGlide(context: Context, imgUri: Uri?, imgView: ImageView) {
        Glide.with(context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
            )
            .into(imgView)
    }

    fun oneOrMultiple(data: Intent?){
        if (data != null){
            showToast("He llegado")
            var text: String = ""
            var data: ClipData = data.clipData!!
                for (i in 0 until data.itemCount){
                    var carro = data.getItemAt(i).uri
                    //printImageWithGlide(this, carro , this.findViewById<ImageView>(R.id.show_picture_imageview))
                    text = "numero ${data.itemCount} de tipo  ${carro is Uri}"
                }
            showToast(text)
        }
    }

}






package com.example.testingenvironment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


const val REQUEST_IMAGE_GET = 1


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.findViewById<Button>(R.id.get_image_button).setOnClickListener {
            selectImage()
        }
    }

    fun getImageButtonAction() {

    }

    fun showToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    fun createIntent(action: String, typeString: String): Intent{
        return Intent(action).apply {
            type = typeString
        }
    }



    fun selectImage() {
        intent = createIntent(Intent.ACTION_GET_CONTENT, "image/*")
        //if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET)
        //}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            //val thumbnail: Bitmap = data.getParcelableExtra("data")
            val fullPhotoUri: Uri? = data!!.data
            printImageWithGlide(this, fullPhotoUri, this.findViewById<ImageView>(R.id.show_picture_imageview))
            // Do work with photo saved at fullPhotoUri
        }
    }

    fun printImageWithGlide(context: Context, imgUri: Uri?, imgView: ImageView){
        Glide.with(context)
            .load(imgUri)
            .apply(
                RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background))
            .into(imgView)
    }

}
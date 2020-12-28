package com.example.testingenvironment.imagelist

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapRegionDecoder
import android.graphics.Rect
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


fun saveImageToInternalStorage(uri: Uri, context: Context): Uri {
    // Get the image from drawable resource as drawable object
    //val drawable = ContextCompat.getDrawable(context, 1)
    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)

    //these 2 lines are in case the image is too large or big
    //val decoder: BitmapRegionDecoder = BitmapRegionDecoder.newInstance(uri.path, false)
    //val region = decoder.decodeRegion(Rect(10, 10, 50, 50), null)

    // Get the context wrapper instance
    val wrapper = ContextWrapper(context)

    // Initializing a new file
    // The bellow line return a directory in internal storage
    var file = wrapper.getDir("images", Context.MODE_PRIVATE)


    // Create a file to save the image
    //UUID is used for for creating random file names
    file = File(file, "${UUID.randomUUID()}.jpg")

    try {
        // Get the file output stream
        val stream: OutputStream = FileOutputStream(file)

        // Compress bitmap
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

        // Flush the stream
        stream.flush()

        // Close stream
        stream.close()
    } catch (e: IOException){ // Catch the exception
        e.printStackTrace()
    }

    // Return the saved image uri
    return Uri.parse(file.absolutePath)
}

fun createCustomBitmap(uri: Uri){


}
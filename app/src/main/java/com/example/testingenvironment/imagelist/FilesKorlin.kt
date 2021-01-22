package com.example.testingenvironment.imagelist

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


fun saveImageToInternalStorage(uri: Uri, context: Context): Uri {

    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)

    val wrapper = ContextWrapper(context)

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

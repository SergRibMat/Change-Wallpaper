package com.example.testingenvironment.worker

import android.app.WallpaperManager
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.example.testingenvironment.MainActivity

class SetWallpaperWorker (appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {

        // ADD THIS LINE
        val resourceUri = inputData.getString("1").toString()

        val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(applicationContext)
        if (wallpaperManager.isSetWallpaperAllowed) {
            wallpaperManager.setBitmap(getImageWithGlide(resourceUri))
        }

        Result.retry()

        return try {
            Result.success()
        }catch (e: Exception){
            Result.failure()
        }
    }

    private fun getImageWithGlide(resourceUri: String) = Glide.with(MainActivity.applicationContext())
        .asBitmap()
        .load(resourceUri)
        .submit()
        .get()


}
package sergio.ribera.testingenvironment.worker

import android.app.WallpaperManager
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import sergio.ribera.testingenvironment.MainActivity
import sergio.ribera.testingenvironment.database.ImageUriDatabase

class SetWallpaperWorker (appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {



    override suspend fun doWork(): Result {

        //reference to the data sourse
        val albumGroup = inputData.getString("album")
        val dataSource = ImageUriDatabase.getInstance(applicationContext).imageUriDatabaseDao
        //Log.i("SetWallpaperWorker", "Album id =  $albumGroup")
        val albumList =dataSource.getImagesFromAlbum(albumGroup!!.toInt())

        try{
            val albumSize = albumList.size

            if (!albumBiggerThan(albumSize, 2)){
                //Log.i("SetWallpaperWorker", "size of album $albumSize and it wasn't executed" )
                return Result.success()
            }
            val imageUriObject = dataSource.getImageUriById(generateRandomNumber(albumSize))
            //save the used images in the database so there is no repited images
            val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(applicationContext)
            if (wallpaperManager.isSetWallpaperAllowed) {
                wallpaperManager.setBitmap(getImageWithGlide(imageUriObject.pathToImage))
                //Log.i("SetWallpaperWorker", "Changed wallpaper image")
            }
            //Log.i("SetWallpaperWorker", "Executed success")

            return Result.success()
        }catch (e: Exception){
            //Log.i("SetWallpaperWorker", "Executed failure Exception")
            return Result.failure()
        }

        //use 1 different image each time its called"


        //cuando le doy a ON en el switch button, doWork no es ejecutado inmediatamente
        //no se para el work aunque ponga el switch a off
        //El work se inicia al iniciarse la app, independientemente de donde esta, pero no se inician otras clases sin crearlas

        //IMPORTANT. once you start the work, if you dont cancel it before you close the app, the work will execute again
        //the next time you open the app.
        //IMPORTANT. Si abres la app despues de cerrarla sin detener el work, el work se ejecutara nada mas entres,
        //y se seguira ejecutando cada X desde que entras a menos que lo pares
    }

    private fun albumBiggerThan(albumSize: Int, minimum: Int): Boolean = albumSize > minimum

    private fun generateRandomNumber(end: Int): Int = (1..end).random()

    private fun getImageWithGlide(resourceUri: String) = Glide.with(MainActivity.applicationContext())
        .asBitmap()
        .load(resourceUri)
        .submit()
        .get()


}
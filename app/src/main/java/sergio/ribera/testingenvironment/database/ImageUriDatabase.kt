package sergio.ribera.testingenvironment.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Album::class, ImageUri::class, OptionsData::class], version = 4,  exportSchema = false)
abstract class ImageUriDatabase : RoomDatabase(){

    abstract val imageUriDatabaseDao: ImageUriDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: ImageUriDatabase? = null

        fun getInstance(context: Context): ImageUriDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ImageUriDatabase::class.java,
                        "ImageUriDatabase"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance

                }
                return instance
            }
        }
    }
}
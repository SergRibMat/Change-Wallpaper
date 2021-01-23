package sergio.ribera.testingenvironment.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "options_data")
data class OptionsData (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,
    @ColumnInfo(name = "is_selected")
    var isSelected: Boolean,
    @ColumnInfo(name = "selected_album")
    var selectedAlbum: String,
    @ColumnInfo(name = "time")
    var time: Long, //in num. It can be 20 because its minutes or 1 because its days
    @ColumnInfo(name = "time_unit_int")
    var timeUnitInt: Int //DAY -> 1, HOUR -> 2, MINUTE -> 0
)
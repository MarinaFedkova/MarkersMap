package ru.maggy.markersmap.db

import android.content.Context
import androidx.room.*
import com.google.android.gms.maps.model.LatLng
import ru.maggy.markersmap.dao.MarkerDao
import ru.maggy.markersmap.entity.MarkerEntity

@Database(entities = [MarkerEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDb: RoomDatabase() {
    abstract fun markerDao(): MarkerDao

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDb::class.java, "app.db")
                .build()
    }
}
class Converters {
    @TypeConverter
    fun fromLatLngToString(value: LatLng): String {
        val list = value.let { listOf(it.latitude.toString(), it.longitude.toString()) }
        return list.joinToString(",")
    }

    @TypeConverter
    fun fromStringToLatLng(value: String): LatLng {
        val list = value.split(",")
        return list.let {
            LatLng(value[0].code.toDouble(), value[1].code.toDouble())
        }
    }


}


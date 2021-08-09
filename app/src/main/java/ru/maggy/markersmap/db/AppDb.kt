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
    fun fromLatLng(value: List<Double>): LatLng {
        return  value.let {
            LatLng (value[0], value[1]) }
    }

    @TypeConverter
    fun dateToLatLng(position: LatLng): List<Double> {
        return position.let { listOf(it.latitude, it.longitude) }
    }
}
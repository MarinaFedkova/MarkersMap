package ru.maggy.markersmap.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.maggy.markersmap.dao.MarkerDao
import ru.maggy.markersmap.entity.MarkerEntity

@Database(entities = [MarkerEntity::class], version = 1, exportSchema = false)

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
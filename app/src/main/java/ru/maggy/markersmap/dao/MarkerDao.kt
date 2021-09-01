package ru.maggy.markersmap.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.maggy.markersmap.db.Converters
import ru.maggy.markersmap.entity.MarkerEntity

@Dao
@TypeConverters(Converters::class)
interface MarkerDao {

    @Query("SELECT * FROM MarkerEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<MarkerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(marker: MarkerEntity)

    @Query("DELETE FROM MarkerEntity WHERE id = :id")
    suspend fun deleteById(id: Int)
}
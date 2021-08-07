package ru.maggy.markersmap.repository

import androidx.lifecycle.LiveData
import ru.maggy.markersmap.dto.Marker

interface MarkerRepository {
    val data: LiveData<List<Marker>>

    suspend fun getAll()
    suspend fun save(marker: Marker)
    suspend fun deleteById(id: Int)
}
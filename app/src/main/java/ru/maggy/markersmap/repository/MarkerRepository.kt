package ru.maggy.markersmap.repository

import androidx.lifecycle.LiveData
import ru.maggy.markersmap.dto.Marker

interface MarkerRepository {

    fun getAll(): LiveData<List<Marker>>
    suspend fun save(marker: Marker)
    suspend fun deleteById(id: Int)
}
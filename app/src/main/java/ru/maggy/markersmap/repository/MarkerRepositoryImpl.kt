package ru.maggy.markersmap.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.maggy.markersmap.dao.MarkerDao
import ru.maggy.markersmap.dto.Marker
import ru.maggy.markersmap.entity.MarkerEntity
import ru.maggy.markersmap.entity.toDto

class MarkerRepositoryImpl(
    private val dao: MarkerDao
): MarkerRepository {

    override val data = dao.getAll().map(List<MarkerEntity>::toDto)

    override suspend fun save(marker: Marker) {
        try {
            dao.insert(MarkerEntity.fromDto(marker))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun deleteById(id: Int) {
       try {
           dao.deleteById(id)
       } catch (e: Exception) {
           e.printStackTrace()
       }
    }
}
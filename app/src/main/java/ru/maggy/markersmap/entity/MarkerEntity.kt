package ru.maggy.markersmap.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import ru.maggy.markersmap.dto.Marker

@Entity
data class MarkerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String?,
    val position: LatLng
) {
    fun toDto() = Marker(
        id,
        title,
        position
    )

    companion object {
        fun fromDto(dto: Marker) =
            MarkerEntity(
                dto.id,
                dto.title,
                dto.position,
            )
    }
}


fun List<MarkerEntity>.toDto(): List<Marker> = map(MarkerEntity::toDto)

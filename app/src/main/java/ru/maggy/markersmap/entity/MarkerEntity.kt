package ru.maggy.markersmap.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.android.gms.maps.model.LatLng
import ru.maggy.markersmap.db.Converters
import ru.maggy.markersmap.dto.Marker

@Entity

data class MarkerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String?,
    @TypeConverters(Converters::class)
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
fun List<Marker>.toEntity(): List<MarkerEntity> = map(MarkerEntity::fromDto)

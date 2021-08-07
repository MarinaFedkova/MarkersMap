package ru.maggy.markersmap.dto

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class Marker(
    val id: Int,
    val title: String?,
    val position: LatLng,
) : Parcelable
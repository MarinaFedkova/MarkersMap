package ru.maggy.markersmap.model

import ru.maggy.markersmap.dto.Marker


data class FeedModel(
    val markers: List<Marker> = emptyList(),
    val empty: Boolean = false,
    val error: Boolean = false
)
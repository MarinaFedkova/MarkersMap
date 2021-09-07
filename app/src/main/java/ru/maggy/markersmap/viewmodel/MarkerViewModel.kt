package ru.maggy.markersmap.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import ru.maggy.markersmap.db.AppDb
import ru.maggy.markersmap.dto.Marker
import ru.maggy.markersmap.repository.MarkerRepository
import ru.maggy.markersmap.repository.MarkerRepositoryImpl

private var emptyMarker = Marker(0, "", LatLng(0.0, 0.0))

class MarkerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MarkerRepository =
        MarkerRepositoryImpl(AppDb.getInstance(application).markerDao())

    val data = repository.data

    val edited = MutableLiveData(emptyMarker)

    fun saveMarker() {

        edited.value?.let {
            viewModelScope.launch {
                try {
                    repository.save(it)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        edited.value = emptyMarker
    }

    fun changeContent(title: String) {
        val text = title.trim()
        if (edited.value?.title == text) {
            return
        }
        edited.value = edited.value?.copy(
            title = text
        )
    }

    fun deleteMarker(id: Int) {
        edited.value?.let {
            viewModelScope.launch {
                try {
                    repository.deleteById(id)
                } catch (e: Exception) {
                   e.printStackTrace()
                }
            }
        }
        edited.value = emptyMarker
    }

    fun edit(marker: Marker) {
        edited.value = marker
    }

    fun changePosition(position: LatLng) {
        emptyMarker = emptyMarker.copy(position = position)
    }

}
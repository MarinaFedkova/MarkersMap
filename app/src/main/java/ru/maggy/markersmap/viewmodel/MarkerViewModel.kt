package ru.maggy.markersmap.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import ru.maggy.markersmap.db.AppDb
import ru.maggy.markersmap.dto.Marker
import ru.maggy.markersmap.model.FeedModel
import ru.maggy.markersmap.repository.MarkerRepository
import ru.maggy.markersmap.repository.MarkerRepositoryImpl

private val emptyMarker = Marker(0, "", LatLng(0.0, 0.0))

class MarkerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MarkerRepository =
        MarkerRepositoryImpl(AppDb.getInstance(context = application).markerDao())

    val data: LiveData<FeedModel> = repository.data.map(::FeedModel)

    private val _dataState = MutableLiveData<FeedModel>()
    val dataState: LiveData<FeedModel>
        get() = _dataState

    val edited = MutableLiveData(emptyMarker)

    init {
        loadMarkers()
    }

    fun loadMarkers() = viewModelScope.launch {
        try {
            repository.getAll()
            _dataState.value = FeedModel()
        } catch (e: Exception) {
            _dataState.value = FeedModel(error = true)
        }
    }

    fun saveMarker() {
        edited.value?.let {
            viewModelScope.launch {
                try {
                    repository.save(it)
                    _dataState.value = FeedModel()
                } catch (e: Exception) {
                    _dataState.value = FeedModel(error = true)
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
                    _dataState.value = FeedModel()
                } catch (e: Exception) {
                    _dataState.value = FeedModel(error = true)
                }
            }
        }
        edited.value = emptyMarker
    }

    fun edit(marker: Marker) {
        edited.value = marker
    }

}
package com.kls.cvschallenge.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kls.cvschallenge.data.FlickrImage
import com.kls.cvschallenge.data.Result
import com.kls.cvschallenge.repo.FlickrRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val flickrRepository: FlickrRepository): ViewModel() {

    // 2025/1/16 K.S. - Using MutableStateFlow/StateFlow to feed results to UI
    private val _images = MutableStateFlow<Result<List<FlickrImage>>>(Result.Success(emptyList()))
    val images: StateFlow<Result<List<FlickrImage>>> = _images

    private var currentJob: Job? = null

    init {
        currentJob?.cancel()

        // 2025/1/16 K.S. - Maybe taken too literally, but showing Porcupine results initially.
        currentJob = viewModelScope.launch {
            flickrRepository.getFlickrItems("Porcupine").collect { result ->
                when (result) {
                    is Result.Success -> _images.value = Result.Success(result.data.items)
                    is Result.Error -> _images.value = Result.Error(result.exception)
                    Result.Loading -> _images.value = Result.Loading
                }
            }
        }
    }

    fun searchFlickrImages(search: String) {
        // 2025/1/16 K.S. - Want to cancel job if already running, because user could rapid type searches in.
        currentJob?.cancel()

        currentJob = viewModelScope.launch {
            flickrRepository.getFlickrItems(search).collect { result ->
                when (result) {
                    is Result.Success -> _images.value = Result.Success(result.data.items)
                    is Result.Error -> _images.value = Result.Error(result.exception)
                    Result.Loading -> _images.value = Result.Loading
                }
            }
        }
    }
}
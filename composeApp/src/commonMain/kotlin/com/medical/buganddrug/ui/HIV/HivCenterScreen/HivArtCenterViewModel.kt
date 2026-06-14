package com.medical.buganddrug.ui.HIV.HivCenterScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medical.buganddrug.data.model.hivCenterModel.HivArtCenter
import com.medical.buganddrug.data.repository.QuestionsRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch



class HivArtCenterViewModel (
    private val repository: QuestionsRepository
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _centers = MutableStateFlow<List<HivArtCenter>>(emptyList())
    val centers: StateFlow<List<HivArtCenter>> = _centers

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadCenters() {
        viewModelScope.launch {
            _loading.value = true

            repository.fetchCenters()
                .onSuccess {
                    _centers.value = it
                    _error.value = null
                }
                .onFailure {
                    _error.value = it.message
                }

            _loading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }
}

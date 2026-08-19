package com.medical.buganddrug.ui.HIV.HivCenterScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medical.buganddrug.data.model.LocalStorageDatamodel.HivArtCenter

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

           val result = repository.getLocalHivArtCenterList()
                if (result != null) {
                    _centers.value = result.hivArtCenters
                    _error.value = null
                }
                else {
                    _error.value = "no data found"
                }

            _loading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }
}

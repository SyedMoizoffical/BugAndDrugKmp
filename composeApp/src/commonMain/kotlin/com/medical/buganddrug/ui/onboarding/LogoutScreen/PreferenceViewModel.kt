package com.medical.buganddrug.ui.onboarding.LogoutScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medical.buganddrug.data.remote.SharedPreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject

class LogoutViewModel(
    private val sharedPrefs: SharedPreferenceManager
) : ViewModel() {

    private val _patientData = MutableStateFlow<String?>(null)
    val patientData: StateFlow<String?> = _patientData

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun clearEmail() {
        viewModelScope.launch {
            _loading.value = true

            try {
                val data = sharedPrefs.clearEmail()
                _patientData.value = data.toString()
            } catch (e: Exception) {
                _patientData.value = null
            }

            _loading.value = false
        }
    }

    fun clearData() {
        _patientData.value = null
    }
}
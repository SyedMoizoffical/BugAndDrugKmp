package com.medical.buganddrug.ui.AntimicrobialSpectrumScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medical.buganddrug.data.model.AntimicrobialSpectrumData.AntimicrobialSpectrumModel
import com.medical.buganddrug.data.remote.SharedPreferenceManager
import com.medical.buganddrug.data.repository.QuestionsRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


class AntimicrobialSpectrumViewModel (
    private val repository: QuestionsRepository,
    private val sharedPrefs: SharedPreferenceManager
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading


    var getSyndromeIdentificationData by mutableStateOf<AntimicrobialSpectrumModel?>(null) // ✅ since repo returns Result<Unit>
        private set


    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _indWindingData = MutableStateFlow<String?>(null)
    val indWindingData: StateFlow<String?> = _indWindingData





    fun getAntimicrobialSpectrumData() {
        viewModelScope.launch {
            _loading.value = true

            val resultString = sharedPrefs.getPatientData().toString()
            if (resultString.isEmpty() ){
                _errorMessage.value = "Please fill in patient details first"

            }else{



                val result = repository.getAntimicrobialSpectrumData()

                result.onSuccess {
                    _loading.value = false
                    getSyndromeIdentificationData = it // ✅ Unit
                    _errorMessage.value = null
                }.onFailure { throwable ->
                    _loading.value = false
                    _errorMessage.value = throwable.message
                }
            }



        }
    }




    fun clearError() {
        _errorMessage.value = null
    }
    fun clearQsofaResponse() {
        getSyndromeIdentificationData = null
    }

}

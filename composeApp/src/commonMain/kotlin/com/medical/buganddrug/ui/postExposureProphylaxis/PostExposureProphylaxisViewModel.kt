package com.medical.buganddrug.ui.postExposureProphylaxis

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medical.buganddrug.data.model.QoestionsModel.Q4Model.GetPrecautionFinderList
import com.medical.buganddrug.data.model.postExosureProplaxisModel.ExposureProPhylaxisModel
import com.medical.buganddrug.data.remote.SharedPreferenceManager
import com.medical.buganddrug.data.repository.QuestionsRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.launch



class PostExposureProphylaxisViewModel (
    private val repository: QuestionsRepository,
    private val sharedPrefs: SharedPreferenceManager
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading


    var getPrecautionFinderList by mutableStateOf<ExposureProPhylaxisModel?>(null) // ✅ since repo returns Result<Unit>
        private set


    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage




    fun getExposureProPhylaxisModel() {
        viewModelScope.launch {
            _loading.value = true

            println(sharedPrefs.getPatientData())


            val result = repository.getExposureProPhylaxisModel()

            result.onSuccess {
                _loading.value = false
                getPrecautionFinderList = it // ✅ Unit
                _errorMessage.value = null
            }.onFailure { throwable ->
                _loading.value = false
                _errorMessage.value = throwable.message
            }


        }
    }




    fun clearError() {
        _errorMessage.value = null
    }
    fun clearQsofaResponse() {
        getPrecautionFinderList = null
    }

}

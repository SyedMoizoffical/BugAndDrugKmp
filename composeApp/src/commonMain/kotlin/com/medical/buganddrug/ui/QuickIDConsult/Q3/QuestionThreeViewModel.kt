package com.medical.buganddrug.ui.QuickIDConsult.Q3

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medical.buganddrug.data.model.QoestionsModel.Q3Model.GetIVtoPOsData
import com.medical.buganddrug.data.model.QoestionsModel.Q5Model.AntibioticDose
import com.medical.buganddrug.data.remote.SharedPreferenceManager
import com.medical.buganddrug.data.repository.QuestionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.launch

class QuestionThreeViewModel (
    private val repository: QuestionsRepository,
    private val sharedPrefs: SharedPreferenceManager
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading


    var getSyndromeIdentificationData by mutableStateOf<GetIVtoPOsData?>(null) // ✅ since repo returns Result<Unit>
        private set
    var antibioticDoses by mutableStateOf<List<AntibioticDose>>(emptyList())
        private set

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage




    fun getQ3Data() {
        viewModelScope.launch {
            _loading.value = true

            println(sharedPrefs.getPatientData())


            val result = repository.getQ3Data()

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

    fun getAntibiotic() {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.getAntibiotic()
            result.onSuccess {
                _loading.value = false
                antibioticDoses = it!!.antibioticDoses
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
        getSyndromeIdentificationData = null
    }

}

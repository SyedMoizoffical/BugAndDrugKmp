package com.medical.buganddrug.ui.QuickIDConsult.Q5

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medical.buganddrug.data.model.QoestionsModel.Q5Model.AntibioticDose
import com.medical.buganddrug.data.model.QoestionsModel.Q5Model.RenalFunctionCategory
import com.medical.buganddrug.data.model.Survey.AppSurveyPostRequest
import com.medical.buganddrug.data.remote.SharedPreferenceManager
import com.medical.buganddrug.data.repository.QuestionsRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.launch



class AppSurveyViewModel (
    private val repository: QuestionsRepository,
    private val sharedPrefs: SharedPreferenceManager
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    var renalCategories by mutableStateOf<List<RenalFunctionCategory>>(emptyList())
        private set

    var antibioticDoses by mutableStateOf<List<AntibioticDose>>(emptyList())
        private set


    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage
    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage




    fun getAntibioticForSurvey() {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.getAntibioticForSurvey()
            result.onSuccess {
                _loading.value = false
                renalCategories = it!!.renalFunctionCategories
                antibioticDoses = it!!.antibioticDoses
                _errorMessage.value = null
            }.onFailure { throwable ->
                _loading.value = false
                _errorMessage.value = throwable.message
            }
        }
    }

    fun submitSurvey(request: AppSurveyPostRequest) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.submitSurvey(request)
            result.onSuccess {
                _loading.value = false
                _errorMessage.value = null
                _successMessage.value = it.msg
            }.onFailure {
                _loading.value = false
                _errorMessage.value = it.message
            }
        }
    }



    fun clearError() {
        _errorMessage.value = null
    }


}

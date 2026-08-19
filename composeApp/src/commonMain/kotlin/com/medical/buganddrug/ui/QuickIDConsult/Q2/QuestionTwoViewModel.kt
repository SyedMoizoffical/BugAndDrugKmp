package com.medical.buganddrug.ui.QuickIDConsult.Q2

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medical.buganddrug.data.model.LocalStorageDatamodel.SyndromeIdentificationDataQ2
import com.medical.buganddrug.data.model.QoestionsModel.Q2Model.QuestionTwoResponseModel
import com.medical.buganddrug.data.model.patientinfoModel.Data
import com.medical.buganddrug.data.remote.SharedPreferenceManager
import com.medical.buganddrug.data.repository.QuestionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


class QuestionTwoViewModel(
    private val repository: QuestionsRepository,
    private val sharedPrefs: SharedPreferenceManager
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading


    var userState by mutableStateOf<Data?>(null)
        private set
    var patientInfoResponse by mutableStateOf<Unit?>(null) // ✅ since repo returns Result<Unit>
        private set

    var getSyndromeIdentificationData by mutableStateOf<SyndromeIdentificationDataQ2?>(null) // ✅ since repo returns Result<Unit>
        private set


    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _indWindingData = MutableStateFlow<String?>(null)
    val indWindingData: StateFlow<String?> = _indWindingData





    fun getQ2Data() {
        viewModelScope.launch {
            _loading.value = true


            val result = repository.getLocalSyndromeIdentificationDataQ2()

            if (result != null) {
                _loading.value = false
                getSyndromeIdentificationData = result // ✅ Unit
                _errorMessage.value = null
            }else {
                _loading.value = false
                _errorMessage.value = "no data found"
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

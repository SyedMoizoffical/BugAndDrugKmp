package com.medical.buganddrug.ui.QuickIDConsult.Q1

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medical.buganddrug.data.model.QoestionsModel.Q1Model.NewsItem
import com.medical.buganddrug.data.model.QoestionsModel.Q1Model.NewsPostRequest
import com.medical.buganddrug.data.model.QoestionsModel.Q1Model.QsofaNewsResponse
import com.medical.buganddrug.data.model.QoestionsModel.Q1QSofaRequestModel
import com.medical.buganddrug.data.remote.SharedPreferenceManager
import com.medical.buganddrug.data.repository.QuestionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


class QuestionViewModel (
    private val repository: QuestionsRepository,
    private val sharedPrefs: SharedPreferenceManager
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading


    var q1QSofaResponse by mutableStateOf<QsofaNewsResponse?>(null) // ✅ since repo returns Result<Unit>
        private set


    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage




    fun submitQ1QSofa(

        respiratoryRate: Int,
        systolicBP: Int,
        gcsScore: Int
    ) {
        viewModelScope.launch {
            _loading.value = true
//            val resultString = sharedPrefs.getPatientData().toString()
//            val jsonObject = Json.parseToJsonElement(resultString).jsonObject
//
//            val sourceId = jsonObject["source"]
//                ?.jsonPrimitive
//                ?.content
//                .orEmpty()
//sourceId is uniq id for user not use becase it cut off the patieny info
            println(sharedPrefs.getPatientData())
            val patientInfo = Q1QSofaRequestModel(
                infoId = "1",
                respiratoryRate=respiratoryRate,
                systolicBP=systolicBP,
                gcsScore=gcsScore

            )

            val result = repository.submitQ1QSofa(patientInfo)

            result.onSuccess {
                _loading.value = false
                q1QSofaResponse = it // ✅ Unit
                _errorMessage.value = null
            }.onFailure { throwable ->
                _loading.value = false
                _errorMessage.value = throwable.message
            }


        }
    }

    fun submitQ1News2(
        Pulse: NewsItem,
        RoomAirOrSupplementalO2: NewsItem,
        RespiratoryRate: NewsItem,
        SystolicBP: NewsItem,
        HypercapnicRespiratoryFailure: NewsItem,
        Temperature: NewsItem,
        Spo2: NewsItem,
        Consciousness: NewsItem
    ) {
        viewModelScope.launch {
            _loading.value = true
            val resultString = sharedPrefs.getPatientData().toString()

//            val jsonObject = JSONObject(resultString)
//            val sourceId = jsonObject.getString("source")
//            val jsonObject = Json.parseToJsonElement(resultString).jsonObject
//
//            val sourceId = jsonObject["source"]
//                ?.jsonPrimitive
//                ?.content
//                .orEmpty()

            val patientInfo = NewsPostRequest(
                infoId = "1",
                Pulse = Pulse,
                RoomAirOrSupplementalO2 = RoomAirOrSupplementalO2,
                RespiratoryRate = RespiratoryRate,
                SystolicBP = SystolicBP,
                HypercapnicRespiratoryFailure = HypercapnicRespiratoryFailure,
                Temperature = Temperature,
                Spo2 = Spo2,
                Consciousness = Consciousness
            )

            val result = repository.submitQ1News2(patientInfo)

            result.onSuccess {
                _loading.value = false
                q1QSofaResponse = it
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
        q1QSofaResponse = null
    }

}

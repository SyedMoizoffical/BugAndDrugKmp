package com.medical.buganddrug.ui.QuickIDConsult.Q1

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medical.buganddrug.data.model.LocalStorageDatamodel.News2ScoringPossibility
import com.medical.buganddrug.data.model.LocalStorageDatamodel.QSofaScoringPossibility
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


    var q1QSofaResponse by mutableStateOf<QSofaScoringPossibility?>(null) // ✅ since repo returns Result<Unit>
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
//
            val patientInfo = Q1QSofaRequestModel(
                infoId = "1",
                respiratoryRate=respiratoryRate,
                systolicBP=systolicBP,
                gcsScore=gcsScore

            )

            val result = repository.getLocalQSofaScoringPossibilities()

            val sum = respiratoryRate + systolicBP + gcsScore

            val response = result?.find {
                it.score?.toIntOrNull() == sum
            }

            _loading.value = false

            if (response != null) {
                q1QSofaResponse = response
                _errorMessage.value = null
            } else {
                _errorMessage.value = "No data found"
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

            val result = repository.getLocalNews2ScoringPossibilities()

            val sum = Pulse.Score+RoomAirOrSupplementalO2.Score +RespiratoryRate.Score+SystolicBP.Score+HypercapnicRespiratoryFailure.Score+Temperature.Score+Spo2.Score+Consciousness.Score

            val response = result?.find {
                it.score == "$sum points"
            }

            _loading.value = false

            if (response != null) {
                val qSofaScoringPossibility = QSofaScoringPossibility(
                     score=response.score,
                 riskLevel=response.riskLevel,
                 clinicalResponse=response.clinicalResponse,
                )
                q1QSofaResponse = qSofaScoringPossibility
                _errorMessage.value = null
            } else {
                _errorMessage.value = "No data found"
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

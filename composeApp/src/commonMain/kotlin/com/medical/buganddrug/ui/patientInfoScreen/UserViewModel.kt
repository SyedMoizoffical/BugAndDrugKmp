package com.medical.buganddrug.ui.patientInfoScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medical.buganddrug.data.model.PatientInfo
import com.medical.buganddrug.data.model.patientinfoModel.Data
import com.medical.buganddrug.data.remote.SharedPreferenceManager
import com.medical.buganddrug.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class UserViewModel (
    private val repository: UserRepository,
    private val sharedPrefs: SharedPreferenceManager
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    var userState by mutableStateOf<Data?>(null)
        private set
    var patientInfoResponse by mutableStateOf<Unit?>(null) // ✅ since repo returns Result<Unit>
        private set

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    fun fetchUser() {
        viewModelScope.launch {
            _loading.value = true

            val result = repository.getUser()
            result.onSuccess {
                _loading.value = false
                userState = it
                _errorMessage.value = null
            }.onFailure { throwable ->
                _loading.value = false
                _errorMessage.value = throwable.message
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    fun submitPatientInfo(
        name: String,
        source: String,
        age: String,
        weight: String,
        symptoms: String,
        recentHospitalStay: String,
        immunoReasons: String,
        sign: String,
        indwelling: String,
        hr: String,
        bpSystolic: String,
        bpDiastolic: String,
        temp: String,
        rr: String,
        diseasesList: String
    ) {
        viewModelScope.launch {
            _loading.value = true
            val now = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())

            val currentDateTime =
                "${now.year}-${now.monthNumber.toString().padStart(2, '0')}-${now.dayOfMonth.toString().padStart(2, '0')}T" +
                        "${now.hour.toString().padStart(2, '0')}:" +
                        "${now.minute.toString().padStart(2, '0')}:" +
                        "${now.second.toString().padStart(2, '0')}"

            val patientInfo = PatientInfo(
                id = 0,
                infoId = source,
                name = name,
                age = age.toIntOrNull() ?: 0,
                weight = weight.toDoubleOrNull() ?: 0.0,
                symptomId = 0,
                symptomName = symptoms,
                reasonId = 0,
                reasonName = immunoReasons,
                seasonId = null,
                seasoncauseName = null,
                season = null,
                hospitalStay = recentHospitalStay,
                heartRate = hr,
                temperature = temp,
                durationofIllness = "",
                bloodPressureSystolic = bpSystolic.ifBlank { null },
                bloodPressureDiastolic = bpDiastolic.ifBlank { null },
                signId = 0,
                signName = sign,
                respiratoryRate = rr,
                comorbidities = "",
                indwellingdevicesprostheticimplants = indwelling.ifBlank { null },
                updDateTime = currentDateTime,
                diseasesList = diseasesList
            )

            val result = repository.submitPatientInfo(patientInfo)

            result.onSuccess {
                _loading.value = false
                patientInfoResponse = it // ✅ Unit
                _errorMessage.value = null
            }.onFailure { throwable ->
                _loading.value = false
                _errorMessage.value = throwable.message
            }
//
//            // Save patient JSON to SharedPrefs
//            val patientData = buildJsonObject {
//                put("source", source)
//                put("age", age)
//                put("weight", weight)
//                put("symptoms", symptoms)
//                put("recentHospitalStay", recentHospitalStay)
//                put("immunoReasons", immunoReasons)
//                put("sign", sign)
//                put("indwelling", indwelling)
//                put("hr", hr)
//                put("bpSystolic", bpSystolic)
//                put("bpDiastolic", bpDiastolic)
//                put("temp", temp)
//                put("rr", rr)
//            }
//            sharedPrefs.savePatientData(patientData)
//            println("Patient JSON: $patientData")
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}

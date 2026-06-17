package com.medical.buganddrug.ui.onboarding.NavDrawer.bugReportScreen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medical.buganddrug.data.model.QoestionsModel.Q2Model.QuestionTwoResponseModel
import com.medical.buganddrug.data.remote.SharedPreferenceManager
import com.medical.buganddrug.data.repository.QuestionsRepository
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.content.PartData

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.utils.io.core.*


class BugReportViewModel(
private val repository: QuestionsRepository,
private val sharedPrefs: SharedPreferenceManager
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success


    fun postBugReport(
        description: String,
        email: String,
        deviceInfo: String,
        images: List<ByteArray>   // ✅ platform already converted
    ) {
        viewModelScope.launch {
            _loading.value = true

            try {

                    val result = repository.postBugReport(
                        description = description,
                        email = email,
                        status = "pending",
                        devicesDetail = deviceInfo,
                        images = images
                    )



                result.onSuccess {
                    _success.value = true
                    _errorMessage.value = null
                }

                result.onFailure {
                    _errorMessage.value = it.message
                }

            } catch (e: Exception) {
                _errorMessage.value = e.message
            }

            _loading.value = false
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun clearSuccess() {
        _success.value = false
    }
}

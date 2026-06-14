package com.medical.buganddrug.ui.CultureGuideTherapy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medical.buganddrug.data.model.CultureTherapyGuideModel.CultureTherapyGuideDto
import com.medical.buganddrug.data.remote.SharedPreferenceManager
import com.medical.buganddrug.data.repository.QuestionsRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.launch


class CultureGuideViewModel (
    private val repository: QuestionsRepository,
    private val sharedPrefs: SharedPreferenceManager
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    private val _cultureGuideList =
        MutableStateFlow<List<CultureTherapyGuideDto>>(emptyList())
    val cultureGuideList: StateFlow<List<CultureTherapyGuideDto>> =
        _cultureGuideList

    fun getCultureGuideData() {
        viewModelScope.launch {
            _loading.value = true

            try {

                val result = repository.getCultureGuideApi()

                result.onSuccess { response ->
                    _cultureGuideList.value =
                        response?.cultureTherapyGuidelistDtos.orEmpty()

                    _errorMessage.value = null
                }.onFailure { throwable ->
                    _errorMessage.value = throwable.message
                }

            } finally {
                _loading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}

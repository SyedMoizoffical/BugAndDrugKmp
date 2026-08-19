package com.medical.buganddrug.ui.EtiologicalAgentScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medical.buganddrug.data.model.LocalStorageDatamodel.SyndromeIdentificationData
import com.medical.buganddrug.data.model.QoestionsModel.Q2Model.QuestionTwoResponseModel
import com.medical.buganddrug.data.remote.SharedPreferenceManager
import com.medical.buganddrug.data.repository.QuestionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.launch

class EtiologicalAgentScreenViewModel (
    private val repository: QuestionsRepository,
    private val sharedPrefs: SharedPreferenceManager
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading


    var getEtiologicalAgent by mutableStateOf<SyndromeIdentificationData?>(null) // ✅ since repo returns Result<Unit>
        private set


    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage




    fun getEtiologicalAgent() {
        viewModelScope.launch {
            _loading.value = true



            val result = repository.getLocalSyndromeIdentificationData()

            if (result != null){
                _loading.value = false
                getEtiologicalAgent = result // ✅ Unit
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
        getEtiologicalAgent = null
    }

}

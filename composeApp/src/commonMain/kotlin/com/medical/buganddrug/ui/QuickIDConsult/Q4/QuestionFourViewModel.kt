package com.medical.buganddrug.ui.QuickIDConsult.Q4

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medical.buganddrug.data.model.QoestionsModel.Q4Model.GetPrecautionFinderList
import com.medical.buganddrug.data.remote.SharedPreferenceManager
import com.medical.buganddrug.data.repository.QuestionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.launch

class QuestionFourViewModel (
    private val repository: QuestionsRepository,
    private val sharedPrefs: SharedPreferenceManager
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading


    var getPrecautionFinderList by mutableStateOf<GetPrecautionFinderList?>(null) // ✅ since repo returns Result<Unit>
        private set


    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage




    fun getQ4Data() {
        viewModelScope.launch {
            _loading.value = true

            println(sharedPrefs.getPatientData())


            val result = repository.getLocalPrecautionFinderList()//getQ4Data()

           if (result!!.isolations.isNotEmpty()) {
                _loading.value = false
                getPrecautionFinderList = result // ✅ Unit
                _errorMessage.value = null
            }else {
                _loading.value = false
                _errorMessage.value = "We couldn't find any data. Please log in again and try once more."
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

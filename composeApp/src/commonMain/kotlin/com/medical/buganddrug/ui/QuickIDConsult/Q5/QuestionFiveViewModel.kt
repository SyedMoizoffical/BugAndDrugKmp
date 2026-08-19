package com.medical.buganddrug.ui.QuickIDConsult.Q5

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medical.buganddrug.data.model.LocalStorageDatamodel.AntibioticDose
import com.medical.buganddrug.data.model.LocalStorageDatamodel.RenalFunctionCategory
import com.medical.buganddrug.data.remote.SharedPreferenceManager
import com.medical.buganddrug.data.repository.QuestionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.launch

class QuestionFiveViewModel (
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




    fun getQ5Data() {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.getLocalCreatinineClearance()
            if (result != null) {
                _loading.value = false
                renalCategories = result!!.renalFunctionCategories
                antibioticDoses = result!!.antibioticDoses
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


}

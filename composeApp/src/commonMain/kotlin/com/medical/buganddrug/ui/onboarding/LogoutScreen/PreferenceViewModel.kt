package com.medical.buganddrug.ui.onboarding.LogoutScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medical.buganddrug.data.remote.SharedPreferenceManager
import com.medical.buganddrug.data.repository.QuestionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LogoutViewModel(
    private val repository: QuestionsRepository,
    private val sharedPrefs: SharedPreferenceManager
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            try {
                // Remove user saved data from shared preferences
                sharedPrefs.clearAll()
                // Remove user cached data from local Room database
                repository.clearLocalData()
            } catch (e: Exception) {
                println("👉 Error during logout cleanup: ${e.message}")
            } finally {
                _loading.value = false
                onComplete()
            }
        }
    }

    fun clearEmail() {
        viewModelScope.launch {
            _loading.value = true
            try {
                sharedPrefs.clearEmail()
            } catch (_: Exception) {
            } finally {
                _loading.value = false
            }
        }
    }

    fun clearData() {
        // legacy method kept for safety
    }
}
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

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun getEmail(): String? = sharedPrefs.getEmail()

    fun clearError() {
        _error.value = null
    }

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

    fun deleteAccount(onSuccess: () -> Unit) {
        val email = sharedPrefs.getEmail() ?: ""
        if (email.isBlank()) {
            _error.value = "User email not found. Please log in again."
            return
        }
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val result = repository.deleteAccount(email)
                result.fold(
                    onSuccess = { response ->
                        if (response.statusCode == 200 || response.success) {
                            sharedPrefs.clearAll()
                            repository.clearLocalData()
                            _loading.value = false
                            onSuccess()
                        } else {
                            _loading.value = false
                            _error.value = response.msg ?: response.statusMessage ?: "Failed to delete account"
                        }
                    },
                    onFailure = { ex ->
                        _loading.value = false
                        _error.value = ex.message ?: "Failed to delete account"
                    }
                )
            } catch (e: Exception) {
                _loading.value = false
                _error.value = e.message ?: "An unexpected error occurred"
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
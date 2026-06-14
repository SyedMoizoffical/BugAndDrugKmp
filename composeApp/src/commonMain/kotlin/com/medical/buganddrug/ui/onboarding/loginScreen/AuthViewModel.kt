package com.medical.buganddrug.ui.onboarding.loginScreen// package com.medical.buganddrug.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medical.buganddrug.data.remote.SharedPreferenceManager
import com.medical.buganddrug.data.repository.QuestionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel (
    private val repository: QuestionsRepository,
    private val sharedPrefs: SharedPreferenceManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()


    fun signInWithEmail(email:String) {
        if (email.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Please enter your email")
            return
        }
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {

            val result = repository.checkEmailExists(email) // GET SignIn?email=...

            result.fold(
                onSuccess = { exists ->
                    if(exists.statusCode==0){
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = exists.msg ?: "Sign-up failed"
                        )
                    }else{
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isExistingUser = true
                        )
                        try {
                            sharedPrefs.saveToken(exists.data !!.token)
                            sharedPrefs.saveEmail(email)

                        }catch (_:Exception){

                        }
                    }

                },
                onFailure = { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = throwable.message ?: "Network error"
                    )
                }
            )
        }
    }

    // Called only when user is new (isExistingUser == false)
    fun signUp(name: String,email: String, password: String, pmdc: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = repository.signUp(
                name = name,
                email = email,
                password = password,
                pmdc = pmdc
            )

            result.fold(
                onSuccess = {exists ->
                    if(exists.statusCode==0){
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = exists.msg ?: "Sign-up failed"
                        )
                }else{
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isExistingUser = true
                        )
                        try {
                            sharedPrefs.saveToken(exists.data!!.token)
                            sharedPrefs.saveEmail(email)


                        }catch (_:Exception){

                        }
                }


                },
                onFailure = { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = throwable.message ?: "Sign-up failed"
                    )
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    fun DismissLoader() {
        _uiState.value = _uiState.value.copy(isLoading = false)
    }

    fun clearNavigation() {
        _uiState.value = _uiState.value.copy(navigateToSurvey = false)
    }
}

data class AuthUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isExistingUser: Boolean? = null,     // null = not checked, true = login, false = signup
    val navigateToSurvey: Boolean = false
)
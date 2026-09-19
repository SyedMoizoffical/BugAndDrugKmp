package com.medical.buganddrug.ui.onboarding.loginScreen// package com.medical.buganddrug.ui.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medical.buganddrug.data.model.LocalStorageDatamodel.LocalDataModel
import com.medical.buganddrug.data.model.LocalStorageDatamodel.LocalDataStorageModel
import com.medical.buganddrug.data.model.QoestionsModel.Q2Model.QuestionTwoResponseModel
import com.medical.buganddrug.data.remote.SharedPreferenceManager
import com.medical.buganddrug.data.repository.QuestionsRepository
import com.medical.buganddrug.data.model.LocalStorageDatamodel.DiseaseItem
import com.medical.buganddrug.util.NetworkErrorHandler
import com.medical.buganddrug.util.toUserFriendlyMessage
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
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _diseaseList = MutableStateFlow<List<DiseaseItem>>(emptyList())
    val diseaseList: StateFlow<List<DiseaseItem>> = _diseaseList.asStateFlow()


    var getAllLocalData by mutableStateOf<QuestionTwoResponseModel?>(null) // ✅ since repo returns Result<Unit>
        private set


    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    fun signIn(email: String, password: String) {
        if (email.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Please enter your email")
            return
        }
        if (password.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Please enter your password")
            return
        }
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            val result = repository.login(email.trim(), password)

            result.fold(
                onSuccess = { response ->
                    if (response.statusCode == 200 || response.success) {
                        try {
                            response.data?.token?.let { token ->
                                sharedPrefs.saveToken(token)
                            }
                            sharedPrefs.saveEmail(email.trim())
                            getAllLocalData()
                        } catch (_: Exception) {}

                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isExistingUser = true
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = NetworkErrorHandler.sanitizeMessage(response.msg ?: response.statusMessage ?: "Login failed")
                        )
                    }
                },
                onFailure = { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = throwable.toUserFriendlyMessage()
                    )
                }
            )
        }
    }

    fun signInWithEmail(email: String, password: String = "") {
        signIn(email, password)
    }

    // Called only when user is new (isExistingUser == false)
    fun signUp(name: String, email: String, password: String, pmdc: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = repository.signUp(
                name = name,
                email = email,
                password = password,
                pmdc = pmdc
            )

            result.fold(
                onSuccess = { response ->
                    if (response.statusCode == 200 || response.success) {
                        try {
                            sharedPrefs.saveEmail(email.trim())
                        } catch (_: Exception) {}

                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isOtpSent = true,
                            otpEmail = email.trim(),
                            devOtp = response.data?.devOtp,
                            error = null
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = NetworkErrorHandler.sanitizeMessage(response.msg ?: response.statusMessage ?: "Sign-up failed")
                        )
                    }
                },
                onFailure = { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = throwable.toUserFriendlyMessage()
                    )
                }
            )
        }
    }

    fun getSavedEmail(): String? = sharedPrefs.getEmail()

    fun verifyEmail(otp: String) {
        val email = _uiState.value.otpEmail
        if (email.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Email address is missing. Please try signing up again.")
            return
        }
        if (otp.isBlank() || otp.length < 6) {
            _uiState.value = _uiState.value.copy(error = "Please enter the complete 6-digit OTP code")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = repository.verifyEmail(
                email = email,
                otp = otp.trim()
            )

            result.fold(
                onSuccess = { response ->
                    if (response.statusCode == 200 || response.success) {
                        try {
                            response.data?.token?.let { token ->
                                sharedPrefs.saveToken(token)
                            }
                            sharedPrefs.saveEmail(email.trim())
                            getAllLocalData()
                        } catch (_: Exception) {}

                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isOtpSent = false,
                            isExistingUser = true
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = NetworkErrorHandler.sanitizeMessage(response.msg ?: response.statusMessage ?: "Verification failed")
                        )
                    }
                },
                onFailure = { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = throwable.toUserFriendlyMessage()
                    )
                }
            )
        }
    }

    fun resendOtp() {
        val email = _uiState.value.otpEmail
        if (email.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Email address is missing. Please try signing up again.")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, successMessage = null)

            val result = repository.resendOtp(email = email)

            result.fold(
                onSuccess = { response ->
                    if (response.statusCode == 200 || response.success) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            successMessage = response.msg ?: "OTP sent successfully",
                            error = null
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = NetworkErrorHandler.sanitizeMessage(response.msg ?: response.statusMessage ?: "Failed to resend OTP")
                        )
                    }
                },
                onFailure = { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = throwable.toUserFriendlyMessage()
                    )
                }
            )
        }
    }

    fun resetOtpState() {
        _uiState.value = _uiState.value.copy(isOtpSent = false, devOtp = null, error = null, successMessage = null)
    }

    private val _localDataState = MutableStateFlow<LocalDataModel?>(null)
    val localDataState: StateFlow<LocalDataModel?> = _localDataState.asStateFlow()

    fun getAllLocalData(forceFetch: Boolean = false) {
        viewModelScope.launch {
            if (!forceFetch && repository.hasLocalData()) {
                println("👉 Local data already exists in database. Skipping network fetch.")
                printExtractedDiseaseList()
                return@launch
            }
            _loading.value = true
            val result = repository.getAllLocalData()
            result.onSuccess { data ->
                _loading.value = false
                _errorMessage.value = null
                _localDataState.value = data
                printExtractedDiseaseList()
            }.onFailure { throwable ->
                _loading.value = false
                _errorMessage.value = throwable.toUserFriendlyMessage()
            }
        }
    }

    fun printExtractedDiseaseList() {
        viewModelScope.launch {
            val diseaseList = repository.getDiseaseListFromLocalDatabase()
            _diseaseList.value = diseaseList
            println("👉 PatientTypeSelectionScreen DiseaseExtractor List (Total: ${diseaseList.size}):")
            diseaseList.forEachIndexed { index, item ->
                println("   [$index] name = \"${item.name}\", id = ${item.id}, type = \"${item.type}\"")
            }
        }
    }

    fun checkLocalDataExists() {
        viewModelScope.launch {
            try {
                val hasData = repository.hasLocalData()
                val savedEmail = sharedPrefs.getEmail()
                if (hasData && !savedEmail.isNullOrEmpty()) {
                    _uiState.value = _uiState.value.copy(hasLocalData = true)
                }
            } catch (_: Exception) {
                // If check fails, stay on welcome screen
            }
        }
    }


    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
        _errorMessage.value = null
    }

    fun clearSuccessMessage() {
        _uiState.value = _uiState.value.copy(successMessage = null)
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
    val successMessage: String? = null,
    val isExistingUser: Boolean? = null,     // null = not checked, true = login, false = signup
    val isOtpSent: Boolean = false,          // true when signup 200 returned and waiting for OTP
    val otpEmail: String = "",               // email used during signup
    val devOtp: String? = null,              // devOtp if returned
    val navigateToSurvey: Boolean = false,
    val hasLocalData: Boolean = false
)
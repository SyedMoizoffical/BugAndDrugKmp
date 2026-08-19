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
                            getAllLocalData()
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
                            getAllLocalData()


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
                _errorMessage.value = throwable.message
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
    val navigateToSurvey: Boolean = false,
    val hasLocalData: Boolean = false
)
package com.medical.buganddrug.data.repository

import com.medical.buganddrug.data.model.PatientInfo
import com.medical.buganddrug.data.model.patientinfoModel.Data
import com.medical.buganddrug.data.remote.ApiService
import com.medical.buganddrug.data.remote.NetworkConnectivityChecker
import com.medical.buganddrug.util.NetworkErrorHandler
import com.medical.buganddrug.util.toUserFriendlyMessage

class UserRepository(
    private val api: ApiService,
    private val networkChecker: NetworkConnectivityChecker? = null
) {
    suspend fun getUser(): Result<Data?> {
        if (networkChecker?.isNetworkAvailable() == false) {
            return Result.failure(Exception(NetworkErrorHandler.NO_INTERNET_MESSAGE))
        }
        return try {
            val response = api.getUser()
            if (response.statusCode == 200 || response.success) {
                Result.success(response.data)
            } else if (response.statusCode == 401) {
                Result.failure(Exception(NetworkErrorHandler.SESSION_EXPIRED_MESSAGE))
            } else {
                val errorMsg = NetworkErrorHandler.sanitizeMessage(response.statusMessage ?: response.msg ?: "Request failed")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserFriendlyMessage()))
        }
    }

    suspend fun submitPatientInfo(patientInfo: PatientInfo): Result<Unit> {
        if (networkChecker?.isNetworkAvailable() == false) {
            return Result.failure(Exception(NetworkErrorHandler.NO_INTERNET_MESSAGE))
        }
        return try {
            val response = api.submitPatientInfo(patientInfo)
            if (response.statusCode == 200 || response.success) {
                Result.success(Unit)
            } else if (response.statusCode == 401) {
                Result.failure(Exception(NetworkErrorHandler.SESSION_EXPIRED_MESSAGE))
            } else {
                val errorMsg = NetworkErrorHandler.sanitizeMessage(response.statusMessage ?: response.msg ?: "Request failed")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserFriendlyMessage()))
        }
    }
}

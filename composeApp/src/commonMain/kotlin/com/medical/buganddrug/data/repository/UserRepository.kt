package com.medical.buganddrug.data.repository

import com.medical.buganddrug.data.model.PatientInfo
import com.medical.buganddrug.data.model.patientinfoModel.Data
import com.medical.buganddrug.data.remote.ApiService

class UserRepository(
    private val api: ApiService
) {
    suspend fun getUser(): Result<Data?> {
        return try {
            val response = api.getUser()
            if (response.statusCode == 200) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.statusMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun submitPatientInfo(patientInfo: PatientInfo): Result<Unit> {
        return try {
            val response = api.submitPatientInfo(patientInfo)
            if (response.statusCode == 200) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.statusMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

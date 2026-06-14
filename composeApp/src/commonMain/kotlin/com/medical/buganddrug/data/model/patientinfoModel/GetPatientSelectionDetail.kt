package com.medical.buganddrug.data.model.patientinfoModel
import kotlinx.serialization.Serializable

@Serializable
data class GetPatientSelectionDetail(
    val `data`: Data,
    val msg: String,
    val statusCode: Int,
    val statusMessage: String,
    val success: Boolean
)
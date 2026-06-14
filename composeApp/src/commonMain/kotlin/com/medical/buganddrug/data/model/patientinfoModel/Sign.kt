package com.medical.buganddrug.data.model.patientinfoModel
import kotlinx.serialization.Serializable

@Serializable
data class Sign(
    val signId: Int,
    val signName: String
)
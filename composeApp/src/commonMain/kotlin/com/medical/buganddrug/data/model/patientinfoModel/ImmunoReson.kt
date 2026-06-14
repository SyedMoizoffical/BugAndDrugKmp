package com.medical.buganddrug.data.model.patientinfoModel
import kotlinx.serialization.Serializable

@Serializable
data class ImmunoReson(
    val reasonId: Int,
    val reasonName: String
)
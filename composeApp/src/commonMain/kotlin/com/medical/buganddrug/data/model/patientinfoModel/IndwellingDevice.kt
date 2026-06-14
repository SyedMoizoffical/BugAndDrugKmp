package com.medical.buganddrug.data.model.patientinfoModel
import kotlinx.serialization.Serializable

@Serializable
data class IndwellingDevice(
    val indwellingId: Int,
    val indwellingName: String
)
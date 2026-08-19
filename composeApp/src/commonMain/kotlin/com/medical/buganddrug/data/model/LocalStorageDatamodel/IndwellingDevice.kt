package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable

@Serializable
data class IndwellingDevice(
    val indwellingId: Int? = 0,
    val indwellingName: String? = "",
    val labTest: String? = "",
    val riskOff: String? = "",
)
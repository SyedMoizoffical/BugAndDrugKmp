package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable
@Serializable

data class EtilogicalAgent(
    val alternativeTreatmentOptions: String? = "",
    val firstlineTreatment: String? = "",
    val id: Int? = 0,
    val infectionsCaused: String? = "",
    val organism: String? = "",
    val type: String? = ""
)
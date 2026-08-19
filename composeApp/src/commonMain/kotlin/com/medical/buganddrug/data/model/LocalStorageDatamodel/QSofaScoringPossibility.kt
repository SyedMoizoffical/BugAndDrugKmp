package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable

@Serializable
data class QSofaScoringPossibility(
    val score: String? = "",
    val riskLevel: String? = "",
    val clinicalResponse: String? = ""
)

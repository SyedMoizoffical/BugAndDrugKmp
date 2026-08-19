package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable

@Serializable
data class News2ScoringPossibility(
    val score: String? = "",
    val riskLevel: String? = "",
    val clinicalResponse: String? = "",
    val note: String? = ""
)

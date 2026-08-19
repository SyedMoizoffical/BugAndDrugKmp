package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable
@Serializable

data class SyndromeX(
    val diseaseID: Int? = 0,
    val syndromeId: Int? = 0,
    val syndromeName: String? = ""
)
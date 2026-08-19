package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable
@Serializable

data class SyndromeDisease(
    val disease: String? = "",
    val diseaseID: Int? = 0,
    val syndrome: Syndrome? = null,
    val syndromeID: Int? = 0
)
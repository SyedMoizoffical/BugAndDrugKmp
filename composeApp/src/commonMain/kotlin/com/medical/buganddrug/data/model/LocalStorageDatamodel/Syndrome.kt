package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable
@Serializable

data class Syndrome(
    val id: Int? = 0,
    val syndromeDiseases: List<String?>? = emptyList(),
    val syndromeId: Int? = 0,
    val syndromeName: String? = "",
    val updDateTime: String? = ""
)

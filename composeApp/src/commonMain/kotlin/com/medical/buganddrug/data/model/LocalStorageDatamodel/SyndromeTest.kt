package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable
@Serializable

data class SyndromeTest(
    val diseaseID: Int? = 0,
    val diseaseName: String? = "",
    val id: Int? = 0,
    val syndromeID: Int? = 0,
    val syndromeName: String? = "",
    val testID: Int? = 0,
    val testName: String? = ""
)
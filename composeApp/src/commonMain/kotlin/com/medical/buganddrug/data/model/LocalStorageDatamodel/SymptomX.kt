package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable
@Serializable

data class SymptomX(
    val diseaseSymptoms: String? = "",
    val id: Int?=0,
    val symptomsID: Int?=0,
    val symptomsName: String? = "",
)
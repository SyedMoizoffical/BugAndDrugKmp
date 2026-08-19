package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable
@Serializable

data class DiseaseX(
    val coreType: String? = "",
    val diseaseID: Int?=0,
    val diseaseName: String? = "",
    val isCoreSymptompoint: Int? = 0,
    val symptomsID: Int? = 0,
    val symptomsName: String? = "",
)
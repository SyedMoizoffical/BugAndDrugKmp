package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable
@Serializable

data class DiseaseSymptom(
    val coreType: String? = "",
    val disease: String? = "",
    val diseaseID: Int? = 0,
    val isCoreSymptom: Int? = 0,
    val symptom: SymptomX? = null,
    val symptomsID: Int? = 0
)
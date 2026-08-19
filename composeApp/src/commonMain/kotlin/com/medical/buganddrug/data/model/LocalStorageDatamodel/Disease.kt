package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable

@Serializable
data class Disease(
    val diseaseID: Int? = 0,
    val diseaseName: String? = "",
    val diseaseSymptoms: List<DiseaseSymptom?>? = emptyList(),
    val id: Int? = 0,
    val syndromeDiseases: List<SyndromeDisease?>? = emptyList(),
    val updDateTime: String? = ""
)
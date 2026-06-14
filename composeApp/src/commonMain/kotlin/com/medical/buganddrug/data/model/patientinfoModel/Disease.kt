package com.medical.buganddrug.data.model.patientinfoModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Disease(
    val diseaseID: Int,
    val diseaseName: String,
    val diseaseSymptoms: JsonElement? = null,
    val id: Int,
    val syndromeDiseases: JsonElement? = null,
    val updDateTime: String
)
package com.medical.buganddrug.data.model.patientinfoModel
import kotlinx.serialization.Serializable

@Serializable
data class Symptom(
    val id: Int,
    val symptomsID: Int,
    val symptomsName: String
)
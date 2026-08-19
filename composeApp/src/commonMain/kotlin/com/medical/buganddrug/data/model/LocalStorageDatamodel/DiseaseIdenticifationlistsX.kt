package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable
@Serializable

data class DiseaseIdenticifationlistsX(
    val coreSymptoms: String? = "",
    val diagnosticTests: String? = "",
    val disease: String? = "",
    val diseaseId: Int? = 0,
    val duratioOfTreatment: String? = "",
    val durationofdays: Int? = 0,
    val etiologicalAgent: String? = "",
    val id: Int? = 0,
    val level1classification: String? = "",
    val level1classificationId: Int? = 0,
    val level2classification: String? = "",
    val level2classificationId: Int? = 0,
    val localization: String? = "",
    val localizationId: Int? = 0,
    val optionalSymptoms: String? = "",
    val relevantExposure: String? = "",
    val signs: String? = "",
    val symptomId: String? = "",
    val symptomName: String? = "",
    val testId: Int? = 0,
    val testName: String? = "",
    val treatment: String? = ""
)
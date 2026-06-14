package com.medical.buganddrug.data.model.QoestionsModel.Q2Model

import kotlinx.serialization.Serializable

@Serializable
data class ClinicalSyndromeDiseaseIdentificationLists(
    val id: Int,
    val symptomId: String,
    val symptomName: String,
    val localizationId: Int,
    val localization: String,
    val level1classificationId: Int,
    val level1classification: String,
    val level2classificationId: Int,
    val level2classification: String?, // Nullable
    val diseaseId: Int,
    val disease: String,
    val testId: Int,
    val testName: String?,
    val durationofdays: Int,
    val duratioOfTreatment: String?,
    val etiologicalAgent: String,
    val coreSymptoms: String,
    val optionalSymptoms: String,
    val signs: String,
    val relevantExposure: String,
    val diagnosticTests: String,
    val treatment: String
)
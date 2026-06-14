package com.medical.buganddrug.data.model.QoestionsModel.Q2Model

import kotlinx.serialization.Serializable

@Serializable
data class DiseaseIdentificationLists(
    val disease: String,
    val diseaseId: Int,
    val id: Int,
    val level1classification: String,
    val level1classificationId: Int,
    val level2classification: String,
    val level2classificationId: Int,
    val localization: String,
    val localizationId: Int,
    val symptomId: String,
    val symptomName: String,
    val testId: Int,
    val testName: String?,
    val duratioOfTreatment: String?,
    val durationofdays: Int,
)
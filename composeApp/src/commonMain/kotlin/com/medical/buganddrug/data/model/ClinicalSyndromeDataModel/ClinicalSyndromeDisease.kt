package com.medical.buganddrug.data.model.QoestionsModel.Q2Model

import kotlinx.serialization.Serializable

@Serializable
data class ClinicalSyndromeDisease(
    val coreType: String,
    val diseaseID: Int,
    val diseaseName: String,
    val isCoreSymptompoint: Int,
    val symptomsID: Int
)
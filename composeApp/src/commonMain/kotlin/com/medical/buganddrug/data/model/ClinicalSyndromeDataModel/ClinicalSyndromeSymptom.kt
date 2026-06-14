package com.medical.buganddrug.data.model.QoestionsModel.Q2Model

import kotlinx.serialization.Serializable

@Serializable
data class ClinicalSyndromeSymptom(
    val symptomsID: Int,
    val symptomsName: String
)
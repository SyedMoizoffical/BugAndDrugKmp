package com.medical.buganddrug.data.model.QoestionsModel.Q2Model

import kotlinx.serialization.Serializable

@Serializable
data class ClinicalSyndromeSyndrome(
    val diseaseID: Int,
    val syndromeId: Int,
    val syndromeName: String
)
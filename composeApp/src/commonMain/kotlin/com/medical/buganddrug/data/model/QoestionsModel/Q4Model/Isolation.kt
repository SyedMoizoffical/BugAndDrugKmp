package com.medical.buganddrug.data.model.QoestionsModel.Q4Model

import kotlinx.serialization.Serializable

@Serializable
data class Isolation(
    val diseaseCondition: String,
    val durationofIsolation: String,
    val id: Int,
    val isolationPrecuation: String,
    val pPE: String,
    val transmission: String,
    val typeOfMask: String
)
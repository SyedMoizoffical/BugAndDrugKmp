package com.medical.buganddrug.data.model.QoestionsModel.Q1Model

import kotlinx.serialization.Serializable

@Serializable
data class QsofaNewsResponse(
    val score: String,
    val riskLevel: String,
    val clinicalResponse: String,
    val note: String?,
)

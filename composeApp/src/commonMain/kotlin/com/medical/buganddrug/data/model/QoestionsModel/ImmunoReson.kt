package com.medical.buganddrug.data.model.QoestionsModel

import kotlinx.serialization.Serializable

@Serializable
data class Q1QSofaRequestModel(
    val infoId: String,
    val respiratoryRate: Int,
    val systolicBP: Int,
    val gcsScore: Int,
)
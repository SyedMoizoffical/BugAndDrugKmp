package com.medical.buganddrug.data.model.QoestionsModel.Q3Model

import kotlinx.serialization.Serializable

@Serializable
data class IVtoPO(
    val diseaseId: Int,
    val diseaseName: String,
    val iV: String?,
    val id: Int,
    val poEquivalent: String,
    val switchNote: String?
)
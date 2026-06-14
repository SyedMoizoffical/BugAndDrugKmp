package com.medical.buganddrug.data.model.QoestionsModel.Q2Model
import kotlinx.serialization.Serializable
@Serializable
class ClinicalSyndromeDiseaseSignlist (
    val id: Int,
    val diseaseId: Int,
    val signId: Int,
    val signName: String
)



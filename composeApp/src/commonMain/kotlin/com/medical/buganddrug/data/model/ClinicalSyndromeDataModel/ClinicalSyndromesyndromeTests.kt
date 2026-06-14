package com.medical.buganddrug.data.model.QoestionsModel.Q2Model
import kotlinx.serialization.Serializable
@Serializable
class ClinicalSyndromesyndromeTests (
    val id: Int,
    val syndromeID: Int,
    val syndromeName: String,
    val diseaseID: Int,
    val diseaseName: String,
    val testName: String?,
    val testID: Int
)

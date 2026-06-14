package com.medical.buganddrug.data.model.QoestionsModel.Q2Model
import kotlinx.serialization.Serializable
@Serializable
class ClinicalSyndromeEtilogicalAgentsLists (
    val id: Int,
    val organism: String,
    val type: String,
    val infectionsCaused: String,
    val firstlineTreatment: String,
    val alternativeTreatmentOptions: String?
    )

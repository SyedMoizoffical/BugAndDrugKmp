package com.medical.buganddrug.data.model.CultureTherapyGuideModel

import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class CultureTherapyGuideModel(
    val cultureTherapyGuidelistDtos: List<CultureTherapyGuideDto>
)

@Serializable
data class CultureTherapyGuideDto(
    val id: Int,
    val sample: String,
    val diagnosis: String,
    val organism: String,
    val antibiotic: String,
    val duration: Int,
    val notes: String
)

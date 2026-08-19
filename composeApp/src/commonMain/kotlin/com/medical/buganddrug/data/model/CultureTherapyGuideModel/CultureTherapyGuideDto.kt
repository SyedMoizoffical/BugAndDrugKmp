package com.medical.buganddrug.data.model.CultureTherapyGuideModel

import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class CultureTherapyGuideModel(
    val cultureTherapyGuidelistDtos: List<CultureTherapyGuideDto>
)

@Serializable
data class CultureTherapyGuideDto(
    val antibiotic: String? = "",
    val diagnosis: String? = "",
    val duration: Int? = 0,
    val id: Int? = 0,
    val notes: String? = "",
    val organism: String? = "",
    val sample: String? = ""
)

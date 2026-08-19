package com.medical.buganddrug.data.model.LocalStorageDatamodel
import com.medical.buganddrug.data.model.CultureTherapyGuideModel.CultureTherapyGuideDto
import kotlinx.serialization.Serializable

@Serializable
data class CultureTherapyGuideList(
    val cultureTherapyGuidelistDtos: List<CultureTherapyGuideDto> = emptyList()
)
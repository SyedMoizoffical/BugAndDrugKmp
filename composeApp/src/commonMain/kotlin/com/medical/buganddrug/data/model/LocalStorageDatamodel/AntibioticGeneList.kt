package com.medical.buganddrug.data.model.LocalStorageDatamodel
import com.medical.buganddrug.data.model.QoestionsModel.Q8Model.AntibioticGeneListDto
import kotlinx.serialization.Serializable

@Serializable
data class AntibioticGeneList(
    val antibioticGeneListDtos: List<AntibioticGeneListDto> = emptyList()
)
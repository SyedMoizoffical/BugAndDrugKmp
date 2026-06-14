package com.medical.buganddrug.data.model.QoestionsModel.Q8Model

import kotlinx.serialization.Serializable

@Serializable
data class GetAntibioticGeneListResponse(
    val antibioticGeneListDtos: List<AntibioticGeneListDto>
)

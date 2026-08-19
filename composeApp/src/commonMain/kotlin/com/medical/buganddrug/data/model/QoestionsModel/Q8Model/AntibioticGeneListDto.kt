package com.medical.buganddrug.data.model.QoestionsModel.Q8Model

import kotlinx.serialization.Serializable

@Serializable
data class AntibioticGeneListDto(
    val comments: String? = "",
    val drugofChoice: String? = "",
    val id: Int? = 0,
    val organism: String? = "",
    val resistantGene: String? = "",
    val typicallyResistantAgainst: String? = ""
)

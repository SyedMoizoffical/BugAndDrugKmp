package com.medical.buganddrug.data.model.QoestionsModel.Q8Model

import kotlinx.serialization.Serializable

@Serializable
data class AntibioticGeneListDto(
    val id: Int,
    val organism: String,
    val resistantGene: String,
    val typicallyResistantAgainst: String,
    val drugofChoice: String,
    val comments: String
)

package com.medical.buganddrug.data.model.QoestionsModel.Q3Model

import kotlinx.serialization.Serializable

@Serializable
data class GetIVtoPOsData(
    val iVtoPOs: List<IVtoPO>?,
    val url: String
)
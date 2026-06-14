package com.medical.buganddrug.data.model.QoestionsModel.Q4Model

import kotlinx.serialization.Serializable

@Serializable
data class GetPrecautionFinderList(
    val isolations: List<Isolation>
)
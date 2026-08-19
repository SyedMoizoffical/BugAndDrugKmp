package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable

@Serializable
data class Seasonality(
    val season: String? = "",
    val seasonCauseName: String? = "",
    val seasonId: Int? = 0
)
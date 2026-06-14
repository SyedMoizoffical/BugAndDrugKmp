package com.medical.buganddrug.data.model.patientinfoModel
import kotlinx.serialization.Serializable

@Serializable
data class Seasonality(
    val season: String,
    val seasonCauseName: String,
    val seasonId: Int
)
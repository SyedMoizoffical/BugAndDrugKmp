package com.medical.buganddrug.data.model.hivCenterModel
import kotlinx.serialization.Serializable

@Serializable
data class HivArtCenter(
    val id: Int,
    val province: String,
    val cityId: Int,
    val city: String?,
    val center: String,
    val type: String
)
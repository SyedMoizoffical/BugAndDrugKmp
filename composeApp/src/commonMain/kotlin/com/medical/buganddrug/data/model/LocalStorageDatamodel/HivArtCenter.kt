package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable

@Serializable
data class HivArtCenter(
    val center: String? = "",
    val city: String? = "",
    val cityId: Int? = 0,
    val id: Int? = 0,
    val province: String? = "",
    val type: String? = ""
)
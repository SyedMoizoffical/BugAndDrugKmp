package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable

@Serializable
data class HivArtCenterList(
    val hivArtCenters: List<HivArtCenter> = emptyList()
)
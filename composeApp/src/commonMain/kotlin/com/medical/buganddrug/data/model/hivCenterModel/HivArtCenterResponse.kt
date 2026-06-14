package com.medical.buganddrug.data.model.hivCenterModel
import kotlinx.serialization.Serializable

@Serializable
data class HivArtCenterResponse(
    val hivArtCenters: List<HivArtCenter>
)
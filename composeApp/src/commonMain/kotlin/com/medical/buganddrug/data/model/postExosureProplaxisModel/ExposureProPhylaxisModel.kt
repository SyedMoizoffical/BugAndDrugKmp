package com.medical.buganddrug.data.model.postExosureProplaxisModel
import kotlinx.serialization.Serializable

@Serializable
data class ExposureProPhylaxisModel(
    val exposureProPhylaxisList: List<ExposureProPhylaxis>
)
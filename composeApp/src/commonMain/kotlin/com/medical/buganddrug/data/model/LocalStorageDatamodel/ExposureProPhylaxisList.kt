package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable

@Serializable
data class ExposureProPhylaxisList(
    val exposureProPhylaxisList: List<ExposureProPhylaxis> = emptyList()
)
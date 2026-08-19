package com.medical.buganddrug.data.model.LocalStorageDatamodel
import kotlinx.serialization.Serializable

@Serializable
data class ExposureProPhylaxis(
    val definitionofExposure: String? = "",
    val followUp: String? = "",
    val id: Int? = 0,
    val infection: String? = "",
    val postExposureProphylaxis: String? = "",
    val postExposureRiskAssessment: String? = ""
)
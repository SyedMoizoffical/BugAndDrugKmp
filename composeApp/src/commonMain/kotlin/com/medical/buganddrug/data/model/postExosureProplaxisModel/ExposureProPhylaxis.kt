package com.medical.buganddrug.data.model.postExosureProplaxisModel
import kotlinx.serialization.Serializable

@Serializable
data class ExposureProPhylaxis(
    val definitionofExposure: String,
    val followUp: String,
    val id: Int,
    val infection: String,
    val postExposureProphylaxis: String,
    val postExposureRiskAssessment: String
)
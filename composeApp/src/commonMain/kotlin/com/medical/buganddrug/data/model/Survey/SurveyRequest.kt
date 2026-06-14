package com.medical.buganddrug.data.model.Survey

import kotlinx.serialization.Serializable

@Serializable
data class SurveyRequest(
    val name: String?,
    val designation: String?,
    val department: String?,
    val experience: String?,
    val frequencyGuidelineUse: String?,
    val influencingFactors: Map<String, String>,
    val documentIndication: String?,
    val reviewAfterCulture: String?,
    val changeIfImproving: String?,
    val selectedAntibiotic: String?,
    val barrierToPrescribing: String?,
    val confidentCAIvsHAI: String?,
    val supportDigitalApp: String?,
    val suggestions: String?
)


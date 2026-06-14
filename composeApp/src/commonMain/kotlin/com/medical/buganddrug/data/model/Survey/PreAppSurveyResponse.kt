package com.medical.buganddrug.data.model.Survey
import kotlinx.serialization.Serializable

@Serializable
data class PreAppSurveyResponse(
    val role: String = "",
    val appUsageFrequency: String = "",
    val impactResponses: Map<String, String> = emptyMap(),
    val antibioticInfluence: String = "",
    val usabilityResponses: Map<String, String> = emptyMap(),
    val additionalFeatures: String = "",
    val patientImpactResponses: Map<String, String> = emptyMap(),
    val likedMost: String = "",
    val improvements: String = "",
    val recommend: String = "",
    val otherComments: String = ""
)

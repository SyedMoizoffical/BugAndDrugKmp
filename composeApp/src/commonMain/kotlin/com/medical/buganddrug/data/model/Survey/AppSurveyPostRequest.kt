package com.medical.buganddrug.data.model.Survey
import kotlinx.serialization.Serializable

@Serializable
data class AppSurveyPostRequest(
    val name: String,
    val yearExperience: String,
    val designation: String,
    val department: String,
    val formType: String,
    val questions: List<SurveyQuestionRequest>
)

@Serializable
data class SurveyQuestionRequest(
    val detailId: Int,
    val question: String,
    val type: String,          // radio, dropdown, text
    val options: String,       // comma separated
    val selectedValue: String
)

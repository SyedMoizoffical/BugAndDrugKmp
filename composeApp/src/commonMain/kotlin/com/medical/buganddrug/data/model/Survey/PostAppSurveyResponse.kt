package com.medical.buganddrug.data.model.Survey
import kotlinx.serialization.Serializable

@Serializable
data class PostAppSurveyResponse(
    val name: String,
    val yearExperience: String,
    val designation: String,
    val department: String,
    val formType: String = "postsurvey",
    val questions: List<PostSurveyQuestion>
)

@Serializable
data class PostSurveyQuestion(
    val detailId: Int = 0,
    val question: String,
    val type: String,       // "radio", "dropdown", "text"
    val options: String,    // comma-separated if needed
    val selectedValue: String
)

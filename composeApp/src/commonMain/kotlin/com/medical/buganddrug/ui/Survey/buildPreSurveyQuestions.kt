package com.medical.buganddrug.ui.Survey

import com.medical.buganddrug.data.model.Survey.SurveyQuestionRequest

fun buildPreSurveyQuestions(
    influencingResponses: Map<String, String>,
    documentIndication: String,
    reviewAfterCulture: String,
    changeIfImproving: String,
    selectedAntibiotic: String?,
    barrier: String,
    confidentCAIvsHAI: String,
    supportDigitalApp: String,
    suggestions: String
): List<SurveyQuestionRequest> {

    val questions = mutableListOf<SurveyQuestionRequest>()
    var id = 1

    influencingResponses.forEach { (factor, value) ->
        questions.add(
            SurveyQuestionRequest(
                detailId = id++,
                question = "How often do you consider $factor before prescribing antibiotics?",
                type = "radio",
                options = "Always,Often,Sometimes,Rarely,Never",
                selectedValue = value
            )
        )
    }

    questions.add(
        SurveyQuestionRequest(
            detailId = id++,
            question = "How often do you document indication for antibiotic use?",
            type = "radio",
            options = "Always,Often,Sometimes,Rarely,Never",
            selectedValue = documentIndication
        )
    )

    questions.add(
        SurveyQuestionRequest(
            detailId = id++,
            question = "Do you review antibiotics after culture reports?",
            type = "radio",
            options = "Always,Often,Sometimes,Rarely,Never",
            selectedValue = reviewAfterCulture
        )
    )

    questions.add(
        SurveyQuestionRequest(
            detailId = id++,
            question = "If patient improves, do you change antibiotic?",
            type = "radio",
            options = "Yes,No",
            selectedValue = changeIfImproving
        )
    )

    questions.add(
        SurveyQuestionRequest(
            detailId = id++,
            question = "Selected Antibiotic",
            type = "dropdown",
            options = "",
            selectedValue = selectedAntibiotic.orEmpty()
        )
    )

    questions.add(
        SurveyQuestionRequest(
            detailId = id++,
            question = "Main barriers to appropriate prescribing",
            type = "text",
            options = "",
            selectedValue = barrier
        )
    )

    questions.add(
        SurveyQuestionRequest(
            detailId = id++,
            question = "Confident differentiating CAI vs HAI?",
            type = "radio",
            options = "Yes,No",
            selectedValue = confidentCAIvsHAI
        )
    )

    questions.add(
        SurveyQuestionRequest(
            detailId = id++,
            question = "Support digital app for antibiotic selection?",
            type = "radio",
            options = "Yes,No,Maybe",
            selectedValue = supportDigitalApp
        )
    )

    questions.add(
        SurveyQuestionRequest(
            detailId = id++,
            question = "Suggestions to improve prescribing",
            type = "text",
            options = "",
            selectedValue = suggestions
        )
    )

    return questions
}

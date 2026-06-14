package com.medical.buganddrug.ui.Survey

import com.medical.buganddrug.data.model.Survey.SurveyQuestionRequest

fun buildPostSurveyQuestions(
    role: String,
    otherRole: String,
    usageFrequency: String,
    impactResponses: Map<String, String>,
    antibioticInfluence: String,
    usabilityResponses: Map<String, String>,
    additionalFeatures: String,
    patientImpactResponses: Map<String, String>,
    likedMost: String,
    improvements: String,
    recommend: String,
    otherComments: String
): List<SurveyQuestionRequest> {
    val questions = mutableListOf<SurveyQuestionRequest>()

    // SECTION 1: General Info
    questions.add(
        SurveyQuestionRequest(
            detailId = 0,
            question = "What is your role in the inpatient setting?",
            type = "radio",
            options = "Consultant,Senior Medical Officer,Medical Officer,Other",
            selectedValue = if (role == "Other") otherRole else role
        )
    )

    questions.add(
        SurveyQuestionRequest(
            detailId = 0,
            question = "How often do you use the clinical decision support app in your daily practice?",
            type = "dropdown",
            options = "Very frequently,Frequently,Occasionally,Rarely,Never",
            selectedValue = usageFrequency
        )
    )

    // SECTION 2: Impact on Antibiotic Prescribing
    impactResponses.forEach { (question, answer) ->
        questions.add(
            SurveyQuestionRequest(
                detailId = 0,
                question = question,
                type = "dropdown",
                options = "Strongly agree,Agree,Neutral,Disagree,Strongly disagree",
                selectedValue = answer
            )
        )
    }

    questions.add(
        SurveyQuestionRequest(
            detailId = 0,
            question = "How often did the app influence your choice of antibiotics (e.g., empiric vs. targeted therapy)?",
            type = "dropdown",
            options = "Very frequently,Frequently,Occasionally,Rarely,Never",
            selectedValue = antibioticInfluence
        )
    )

    // SECTION 3: Usability
    usabilityResponses.forEach { (question, answer) ->
        questions.add(
            SurveyQuestionRequest(
                detailId = 0,
                question = question,
                type = "dropdown",
                options = "Very user-friendly,Somewhat user-friendly,Neutral,Somewhat difficult,Very difficult",
                selectedValue = answer
            )
        )
    }

    questions.add(
        SurveyQuestionRequest(
            detailId = 0,
            question = "Do you think the app would be more useful with additional features (e.g., more infection types, real-time updates)?",
            type = "radio",
            options = "Yes,No,Maybe",
            selectedValue = additionalFeatures
        )
    )

    // SECTION 4: Patient Impact
    patientImpactResponses.forEach { (question, answer) ->
        questions.add(
            SurveyQuestionRequest(
                detailId = 0,
                question = question,
                type = "dropdown",
                options = "Strongly agree,Agree,Neutral,Disagree,Strongly disagree",
                selectedValue = answer
            )
        )
    }

    // SECTION 5: Overall Feedback
    questions.add(
        SurveyQuestionRequest(
            detailId = 0,
            question = "What do you like most about the clinical decision support app?",
            type = "text",
            options = "",
            selectedValue = likedMost
        )
    )

    questions.add(
        SurveyQuestionRequest(
            detailId = 0,
            question = "What improvements or changes would you recommend for the app?",
            type = "text",
            options = "",
            selectedValue = improvements
        )
    )

    questions.add(
        SurveyQuestionRequest(
            detailId = 0,
            question = "Would you recommend continuing the use of the clinical decision support app in your practice?",
            type = "radio",
            options = "Yes, strongly recommend,Yes, with reservations,No, would not recommend",
            selectedValue = recommend
        )
    )

    questions.add(
        SurveyQuestionRequest(
            detailId = 0,
            question = "Any other comments or feedback about the app?",
            type = "text",
            options = "",
            selectedValue = otherComments
        )
    )

    return questions
}


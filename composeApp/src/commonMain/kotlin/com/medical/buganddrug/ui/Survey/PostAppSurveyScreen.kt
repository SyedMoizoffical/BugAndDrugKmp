package com.medical.buganddrug.ui.Survey

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.medical.buganddrug.data.model.Survey.PreAppSurveyResponse
import com.medical.buganddrug.ui.QuickIDConsult.topBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction

import com.medical.buganddrug.data.model.Survey.AppSurveyPostRequest
import com.medical.buganddrug.ui.QuickIDConsult.Q5.AppSurveyViewModel
import com.medical.buganddrug.ui.onboarding.GradientButton
import com.medical.buganddrug.util.ErrorAlertDialog
import com.medical.buganddrug.util.LoadingOverlay
import com.medical.buganddrug.util.SuccessAlertDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostAppSurveyScreen(
    viewModel: AppSurveyViewModel,
    onSubmitClick: (PreAppSurveyResponse) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var role by remember { mutableStateOf("") }
    var otherRole by remember { mutableStateOf("") }
    var usageFrequency by remember { mutableStateOf("") }

    val impactResponses = remember { mutableStateMapOf<String, String>() }
    var antibioticInfluence by remember { mutableStateOf("") }

    val usabilityResponses = remember { mutableStateMapOf<String, String>() }
    var additionalFeatures by remember { mutableStateOf("") }

    val patientImpactResponses = remember { mutableStateMapOf<String, String>() }

    var likedMost by remember { mutableStateOf("") }
    var improvements by remember { mutableStateOf("") }
    var recommend by remember { mutableStateOf("") }
    var otherComments by remember { mutableStateOf("") }

    val agreeOptions = listOf("Strongly agree", "Agree", "Neutral", "Disagree", "Strongly disagree")

    val isLoading by viewModel.loading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            topBar(
                topic = "Post-App Survey – Inpatient Prescribers",
                patientType = "",
                onBackClick = onBackClick
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf( Color(0xFFFFFFFF),
                    Color(0xFFF3E5F5))))
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {

            /** PURPOSE + INSTRUCTIONS **/
            item {
                Text(
                    "Purpose:",
                    fontWeight = FontWeight.Bold
                )
                Text("To evaluate the impact of the clinical decision support app on inpatient antibiotic prescribing practices and decision-making.")
                Spacer(Modifier.height(8.dp))
                Text(
                    "Instructions:",
                    fontWeight = FontWeight.Bold
                )
                Text("Please answer the following questions based on your experience using the clinical decision support app for antibiotic prescribing in inpatient settings.")
            }

            /** SECTION 1 **/
            item { SectionHeader("Section 1: General Information") }

            item {
                QuestionCardPost(
                    question = "What is your role in the inpatient setting?",
                    options = listOf("Consultant", "Senior Medical Officer", "Medical Officer", "Other"),
                    selectedOption = role,
                    onOptionSelected = { role = it }
                )
                if (role == "Other") {
                    OutlinedTextField(
                        value = otherRole,
                        onValueChange = { otherRole = it },
                        label = { Text("Please specify") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF800080),
                            unfocusedBorderColor = Color(0xFFBDBDBD),
                            focusedLabelColor = Color(0xFF800080),
                            cursorColor = Color(0xFF800080),

                            // 👇 THIS is what makes background white
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,

                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ) ,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done // sets the "Done" button on the keyboard
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide() // hides the keyboard when Done is pressed
                            }
                        )
                    )
                }
            }

            item {
                QuestionCardPost(
                    question = "How often do you use the clinical decision support app in your daily practice?",
                    options = listOf("Very frequently", "Frequently", "Occasionally", "Rarely", "Never"),
                    selectedOption = usageFrequency,
                    onOptionSelected = { usageFrequency = it }
                )
            }

            /** SECTION 2 **/
            item { SectionHeader("Section 2: Impact on Antibiotic Prescribing") }

            val impactQuestions = listOf(
                "Has the clinical decision support app helped you in making more accurate antibiotic prescribing decisions?",
                "Do you feel more confident in differentiating between bacterial and viral infections after using the app?",
                "Has the app helped you in selecting the correct antibiotic dosage and duration?",
                "Has the app improved your adherence to antibiotic prescribing guidelines?",
                "Do you feel the app has reduced unnecessary antibiotic prescriptions in your practice?"
            )

            items(impactQuestions.size) { i ->
                val q = impactQuestions[i]
                QuestionCardPost(
                    question = q,
                    options = agreeOptions,
                    selectedOption = impactResponses[q],
                    onOptionSelected = { impactResponses[q] = it }
                )
            }

            item {
                QuestionCardPost(
                    question = "How often did the app influence your choice of antibiotics (e.g., empiric vs. targeted therapy)?",
                    options = listOf("Very frequently", "Frequently", "Occasionally", "Rarely", "Never"),
                    selectedOption = antibioticInfluence,
                    onOptionSelected = { antibioticInfluence = it }
                )
            }

            /** SECTION 3 **/
            item { SectionHeader("Section 3: Usability and Features of the App") }

            val usabilityQuestions = listOf(
                "How user-friendly is the clinical decision support app?",
                "Were the app's features (e.g., clinical syndrome identification, antibiotic recommendations) easy to understand and use?",
                "How helpful was the app in helping you identify appropriate antibiotics for specific clinical syndromes?"
            )

            val usabilityOptions = listOf(
                "Very user-friendly",
                "Somewhat user-friendly",
                "Neutral",
                "Somewhat difficult",
                "Very difficult"
            )

            items(usabilityQuestions.size) { i ->
                val q = usabilityQuestions[i]
                QuestionCardPost(
                    question = q,
                    options = usabilityOptions,
                    selectedOption = usabilityResponses[q],
                    onOptionSelected = { usabilityResponses[q] = it }
                )
            }

            item {
                YesNoMaybeQuestionPost(
                    question = "Do you think the app would be more useful with additional features (e.g., more infection types, real-time updates)?",
                    selected = additionalFeatures,
                    onAnswer = { additionalFeatures = it }
                )
            }

            /** SECTION 4 **/
            item { SectionHeader("Section 4: Impact on Patient Care") }

            val patientImpactQuestions = listOf(
                "Has the app improved patient care outcomes related to infections (e.g., reduced treatment failure, fewer adverse effects)?",
                "Do you believe the app has contributed to better infection control and antimicrobial stewardship practices in your institution?",
                "Have you noticed a reduction in the incidence of antibiotic-resistant infections since using the app?"
            )

            items(patientImpactQuestions.size) { i ->
                val q = patientImpactQuestions[i]
                QuestionCardPost(
                    question = q,
                    options = agreeOptions,
                    selectedOption = patientImpactResponses[q],
                    onOptionSelected = { patientImpactResponses[q] = it }
                )
            }

            /** SECTION 5 **/
            item { SectionHeader("Section 5: Overall Feedback") }

            item {
                OutlinedTextField(
                    value = likedMost,
                    onValueChange = { likedMost = it },
                    label = { Text("What do you like most about the clinical decision support app?") },
                    modifier = Modifier.fillMaxWidth()
                    ,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF800080),
                        unfocusedBorderColor = Color(0xFFBDBDBD),
                        focusedLabelColor = Color(0xFF800080),
                        cursorColor = Color(0xFF800080),

                        // 👇 THIS is what makes background white
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,

                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ) ,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done // sets the "Done" button on the keyboard
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide() // hides the keyboard when Done is pressed
                        }
                    )
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = improvements,
                    onValueChange = { improvements = it },
                    label = { Text("What improvements or changes would you recommend for the app?") },
                    modifier = Modifier.fillMaxWidth()
                    ,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF800080),
                        unfocusedBorderColor = Color(0xFFBDBDBD),
                        focusedLabelColor = Color(0xFF800080),
                        cursorColor = Color(0xFF800080),

                        // 👇 THIS is what makes background white
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,

                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ) ,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done // sets the "Done" button on the keyboard
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide() // hides the keyboard when Done is pressed
                        }
                    )
                )
            }

            item {
                QuestionCardPost(
                    question = "Would you recommend continuing the use of the clinical decision support app in your practice?",
                    options = listOf("Yes, strongly recommend", "Yes, with reservations", "No, would not recommend"),
                    selectedOption = recommend,
                    onOptionSelected = { recommend = it }
                )
            }

            item {
                OutlinedTextField(
                    value = otherComments,
                    onValueChange = { otherComments = it },
                    label = { Text("Any other comments or feedback about the app?") },
                    modifier = Modifier.fillMaxWidth()
                    ,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF800080),
                        unfocusedBorderColor = Color(0xFFBDBDBD),
                        focusedLabelColor = Color(0xFF800080),
                        cursorColor = Color(0xFF800080),

                        // 👇 THIS is what makes background white
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,

                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ) ,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done // sets the "Done" button on the keyboard
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide() // hides the keyboard when Done is pressed
                        }
                    )
                )
            }

            /** SUBMIT **/
            item {
                GradientButton(
                    text = "Submit Survey",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onClick = {


                        val request = AppSurveyPostRequest(
                            name = "Ali.Khan@example.com",       // or actual user name/email
                            yearExperience = "8",                // map actual experience
                            designation = "IT",                  // optional if needed
                            department = "Engineering",          // optional
                            formType = "postsurvey",
                            questions = buildPostSurveyQuestions(
                                role = role,
                                otherRole = otherRole,
                                usageFrequency = usageFrequency,
                                impactResponses = impactResponses,
                                antibioticInfluence = antibioticInfluence,
                                usabilityResponses = usabilityResponses,
                                additionalFeatures = additionalFeatures,
                                patientImpactResponses = patientImpactResponses,
                                likedMost = likedMost,
                                improvements = improvements,
                                recommend = recommend,
                                otherComments = otherComments
                            )
                        )

                        viewModel.submitSurvey(request)

                    }
                )
            }

        }
        if (isLoading) {
            LoadingOverlay()
        }
        if (errorMessage != null) {
            ErrorAlertDialog(
                errorMessage = errorMessage,
                onDismiss = { viewModel.clearError() }
            )
        }
        if (successMessage != null) {
            SuccessAlertDialog(
                errorMessage = successMessage,
                onDismiss = { onBackClick() }
            )
        }
    }
}




/* ---------- MODERNIZED COMPONENTS ---------- */

@Composable
fun QuestionCardPost(
    question: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(question, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                options.forEach { opt ->
                    FilterChip(
                        selected = selectedOption == opt,
                        onClick = { onOptionSelected(opt) },
                        label = {
                            Text(
                                opt,
                                color = if (selectedOption == opt) Color.White else Color.Black
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF800080),
                            containerColor = Color(0xFFFFFFFF),
                            selectedLabelColor = Color.White,
                            labelColor = Color.Black
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun YesNoMaybeQuestionPost(
    question: String,
    selected: String?,
    onAnswer: (String) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        Text(question, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(8.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf("Yes", "No", "Maybe").forEach { opt ->
                FilterChip(
                    selected = selected == opt,
                    onClick = { onAnswer(opt) },
                    label = { Text(opt) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF800080),
                        containerColor = Color(0xFFFFFFFF),
                        selectedLabelColor = Color.White,
                        labelColor = Color.Black
                    )
                )
            }
        }
    }
}

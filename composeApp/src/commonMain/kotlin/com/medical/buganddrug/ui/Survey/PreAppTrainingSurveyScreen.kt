package com.medical.buganddrug.ui.Survey

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

import com.medical.buganddrug.data.model.QoestionsModel.Q5Model.AntibioticDose
import com.medical.buganddrug.data.model.Survey.AppSurveyPostRequest
import com.medical.buganddrug.data.model.Survey.SurveyRequest
import com.medical.buganddrug.ui.QuickIDConsult.Q3.SingleSelectSearchableSpinnerDialog
import com.medical.buganddrug.ui.QuickIDConsult.Q5.AppSurveyViewModel
import com.medical.buganddrug.ui.QuickIDConsult.topBar
import com.medical.buganddrug.ui.onboarding.GradientButton
import com.medical.buganddrug.util.ErrorAlertDialog
import com.medical.buganddrug.util.LoadingOverlay
import com.medical.buganddrug.util.MultiSelectSearchableSpinnerDialog
import com.medical.buganddrug.util.SuccessAlertDialog
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.koin.compose.koinInject


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreAppTrainingSurveyScreen(
    viewModel: AppSurveyViewModel = koinInject(),
    onSubmitClick: (SurveyRequest) -> Unit = {},
    onBackClick: () -> Unit = {}
) {

    var name by remember { mutableStateOf("") }
    var designation by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }

    val frequencyOptions = listOf("Always", "Often", "Sometimes", "Rarely", "Never")
    val clinicalSyndromeOptions = listOf("Pyelonephritis", "Sepsis", "Pneumonia", "Meningitis", "Gastroenteritis", "CAUTI", "CLABSI", "Surgical Site Infection", "Skin & Soft Tissue Infection")
    val isLoading by viewModel.loading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val questionList = remember {
        mutableStateListOf(
            PreAppQuestionItem("8a", "Fever, cough, sputum production, abnormal CXR"),
            PreAppQuestionItem("8b", "Fever, dysuria, flank pain, positive urine DR"),
            PreAppQuestionItem("8c", "Fever, chills, hypotension"),
            PreAppQuestionItem("8d", "Fever, loose stools, abdominal cramps"),
            PreAppQuestionItem("8e", "Fever with central line or urinary catheter in place"),
            PreAppQuestionItem("8f", "Fever with central line or urinary catheter in place"),
            PreAppQuestionItem("8g", "Skin redness, swelling, pus discharge"),
            PreAppQuestionItem("8h", "Post-operative fever with no localizing signs"),
            PreAppQuestionItem("8i", "Headache, fever, neck stiffness")
        )
    }
    val resultList = remember {
        mutableStateListOf<PreAppQuestionItem>()
    }
    val influencingFactors = listOf(
        "Clinical experience",
        "Microbiology reports",
        "Peer advice",
        "Guidelines",
        "Patient/family pressure",
        "Antibiotic resistance"
    )
    val influencingResponses = remember { mutableStateMapOf<String, String>() }

    var clinicalSyndrome by remember { mutableStateOf("") }
    var documentIndication by remember { mutableStateOf("") }
    var reviewAfterCulture by remember { mutableStateOf("") }
    var changeIfImproving by remember { mutableStateOf("") }
        //var selectedAntibiotic by remember { mutableStateOf<String?>(null) }

    var barrier by remember { mutableStateOf("") }
    var confidentCAIvsHAI by remember { mutableStateOf("") }
    var supportDigitalApp by remember { mutableStateOf("") }
    var suggestions by remember { mutableStateOf("") }

    LaunchedEffect(Unit) { viewModel.getAntibioticForSurvey() }

    Scaffold(
        topBar = {
            topBar(
                topic = "Bug & Drug Survey",
                patientType = "Pre-App Training (Inpatients)",
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf( Color(0xFFFFFFFF),
                    Color(0xFFF3E5F5))))
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            /** PURPOSE + INSTRUCTIONS **/
            item {
                Text(
                    "Purpose:",
                    fontWeight = FontWeight.Bold
                )
                Text("Antibiotic Prescribing Practices and Clinical Syndrome Assessment – Pre-Training Survey")
                Spacer(Modifier.height(8.dp))
                Text(
                    "Instructions:",
                    fontWeight = FontWeight.Bold
                )
                Text("Please answer the following questions based on your experience using the clinical decision support app for antibiotic prescribing in inpatient settings.")
            }
            // SECTION A ✅
            item {
                SectionHeader("SECTION A: Respondent Information")

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("1. Name (Optional)") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp),
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
                    )
                    ,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done // sets the "Done" button on the keyboard
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide() // hides the keyboard when Done is pressed
                        }
                    )
                )

                QuestionCard(
                    question = "2. Designation",
                    options = listOf("Medical Officer", "Senior Medical Officer", "Consultant","Physician"),
                    selectedOption = designation,
                    onOptionSelected = { designation = it },

                )

                OutlinedTextField(
                    value = department,
                    onValueChange = { department = it },
                    label = { Text("3. Department / Ward") },
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

                OutlinedTextField(
                    value = experience,
                    onValueChange = { experience = it },
                    label = { Text("4. Years of Clinical Experience") },
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

            // SECTION B ✅
            item {
                SectionHeader("SECTION B: Antibiotic Knowledge & Practices")
                //change card view
                SectionHeader("5. How often do you consider the following before prescribing antibiotics?")

//                QuestionCard(
//                    question = "5. How often do you consider the following before prescribing antibiotics?",
//                    options = frequencyOptions,
//                    selectedOption = guidelineFrequency,
//                    onOptionSelected = { guidelineFrequency = it }
//                )
            }

            items(influencingFactors.size) { index ->
                val factor = influencingFactors[index]
                QuestionCard(
                    question = "5. $factor",
                    options = frequencyOptions,
                    selectedOption = influencingResponses[factor],
                    onOptionSelected = { influencingResponses[factor] = it }
                )
            }

            item {
                QuestionCard(
                    question = "6. How often do you document the indication for antibiotic use in the patient notes?",
                    options = frequencyOptions,
                    selectedOption = documentIndication,
                    onOptionSelected = { documentIndication = it }
                )
                   Spacer(modifier = Modifier.height(10.dp))
                QuestionCard(
                    question = "7A. How often do you review the antibiotic therapy after culture reports?",
                    options = frequencyOptions,
                    selectedOption = reviewAfterCulture,
                    onOptionSelected = { reviewAfterCulture = it }
                )
                Spacer(modifier = Modifier.height(10.dp))

//                QuestionCard(
//                    question = "7B. If patient is improving and culture shows other options, do you still change the antibiotic?",
//                    options = yes,
//                    selectedOption = changeIfImproving,
//                    onOptionSelected = { changeIfImproving = it }
//                )
                YesNoQuestion(
                    question = "7B. If patient is improving and culture shows other options, do you still change the antibiotic?",
                    selected = changeIfImproving,
                    onAnswer = { changeIfImproving = it }
                )
            }

            // Antibiotic dropdown
            val antibioticList = viewModel.antibioticDoses.distinctBy { it.antibioticId }

            item {
                Text(
                    "8. Clinical Syndrome and Antibiotic Use Assessment.Identify the clinical syndrome and select appropriate antibiotic",
                    fontWeight = FontWeight.SemiBold
                )
            }
            fun updateResultList(
                resultList: MutableList<PreAppQuestionItem>,
                item: PreAppQuestionItem
            ) {
                val existingIndex = resultList.indexOfFirst { it.id == item.id }

                if (existingIndex != -1) {
                    resultList[existingIndex] = item.copy(
                        selectedOption = item.selectedOption,
                        selectedAntibiotic = item.selectedAntibiotic
                    )
                } else {
                    resultList.add(item)
                }
            }
            items(questionList) { questionItem ->

                QuestionEightCard(
                    question = "${questionItem.id}. ${questionItem.question}",
                    options = clinicalSyndromeOptions,
                    selectedOption = questionItem.selectedOption,
                    onOptionSelected = { selected ->
                        val index = questionList.indexOfFirst { it.id == questionItem.id }

                        if (index != -1) {
                            val updatedItem = questionItem.copy(
                                selectedOption = selected,
                                selectedAntibiotic = null
                            )
                            questionList[index] = updatedItem

                            // ✅ update resultList
                            updateResultList(resultList, updatedItem)
                        }
                    },
                    antibioticList = antibioticList,
                    selectedAntibiotic = questionItem.selectedAntibiotic,
                    onAntibioticSelected = { antibiotic ->
                        val index = questionList.indexOfFirst { it.id == questionItem.id }

                        if (index != -1) {
                            val updatedItem = questionItem.copy(
                                selectedAntibiotic = antibiotic
                            )
                            questionList[index] = updatedItem

                            // ✅ update resultList
                            updateResultList(resultList, updatedItem)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

            }

            // SECTION D ✅
            item {
                SectionHeader("SECTION D: Perceptions & Challenges")

                OutlinedTextField(

                    value = barrier,
                    onValueChange = { barrier = it },
                    label = { Text("9. What do you think are the main barriers to appropriate antibiotic prescribing in your ward?") },
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

                YesNoQuestion(
                    question = "10. Do you feel confident in differentiating between community-acquired and hospital-acquired infections?",
                    selected = confidentCAIvsHAI,
                    onAnswer = { confidentCAIvsHAI = it }
                )

                YesNoMaybeQuestion(
                    question = "11. Do you support the use of a digital app to assist in antibiotic selection?",
                    selected = supportDigitalApp,
                    onAnswer = { supportDigitalApp = it }
                )

                OutlinedTextField(
                    value = suggestions,
                    onValueChange = { suggestions = it },
                    label = { Text("12. Any suggestions to improve antibiotic prescribing in your setting?") },
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

            item {
                GradientButton(
                    text = "Submit Survey",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onClick = {
                        val selectedAntibiotic = Json.encodeToString(resultList.toList())
                        val request = AppSurveyPostRequest(
                            name = name,
                            yearExperience = experience,
                            designation = designation,
                            department = department,
                            formType = "presurvey",
                            questions = buildPreSurveyQuestions(
                                influencingResponses = influencingResponses,
                                documentIndication = documentIndication,
                                reviewAfterCulture = reviewAfterCulture,
                                changeIfImproving = changeIfImproving,
                                selectedAntibiotic = selectedAntibiotic,
                                barrier = barrier,
                                confidentCAIvsHAI = confidentCAIvsHAI,
                                supportDigitalApp = supportDigitalApp,
                                suggestions = suggestions
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

@Serializable
data class PreAppQuestionItem(
    val id: String,
    val question: String,
    var selectedOption: String? = null,
    var selectedAntibiotic: String? = null
)
@Composable
fun SectionHeader(label: String) {
    Text(label, fontWeight = FontWeight.Bold, color = Color(0xFF800080))
    Divider(modifier = Modifier.padding(vertical = 8.dp))
}





@Composable
fun QuestionCard(
    question: String,
    options: List<String>,
    selectedOption: String?, // 👈 added
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
                            selectedContainerColor = Color(0xFF800080),   // dark blue when selected
                            containerColor = Color(0xFFFFFFFF),          // light gray when not selected
                            selectedLabelColor = Color.White,             // text color when selected
                            labelColor = Color.Black                      // text color when not selected
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun QuestionEightCard(
    question: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
    antibioticList: List<AntibioticDose>,
    selectedAntibiotic: String?,
    onAntibioticSelected: (String?) -> Unit
) {
    val selectedImmunoReason = remember { mutableStateOf(listOf<Int>()) }
    var selectedImmunoReasonJsonArray by remember {
        mutableStateOf<JsonArray>(buildJsonArray { })
    }
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(12.dp)) {

            // Question
            Text(
                text = question,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(8.dp))

            // Options Chips
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                options.forEach { opt ->
                    FilterChip(
                        selected = selectedOption == opt,
                        onClick = {
                            onOptionSelected(opt)
                            // optional: reset antibiotic when option changes
                           // onAntibioticSelected(null)
                        },
                        label = {
                            Text(
                                opt,
                                color = if (selectedOption == opt) Color.White else Color.Black
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF800080),
                            containerColor = Color.White,
                            selectedLabelColor = Color.White,
                            labelColor = Color.Black
                        )
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
                MultiSelectSearchableSpinnerDialog(
                    label = "Select Antibiotic",
                    items = antibioticList,
                    itemId = { it.antibioticId },
                    itemLabel = { it.antibioticName },
                    selectedIds = selectedImmunoReason.value
                ) { ids, labels ->

                    selectedImmunoReason.value = ids

                    selectedImmunoReasonJsonArray = buildJsonArray {
                        ids.forEachIndexed { index, id ->
                            add(
                                buildJsonObject {
                                    put("id", id)
                                    put("label", labels.getOrNull(index) ?: "none")
                                }
                            )
                        }
                    }

                    val jsonString = selectedImmunoReasonJsonArray.toString()
                    onAntibioticSelected(jsonString)

                    println("Immunocompromised JSON: $jsonString")
                }


            // Spinner
//            SingleSelectSearchableSpinnerDialog(
//                label = "Select Antibiotic",
//                items = antibioticList.map { it to it },
//                itemLabel = { it.first },
//                selectedItem = selectedAntibiotic,
//                onItemSelected = { sel ->
//                    onAntibioticSelected(sel?.second)
//                }
//            )
        }
    }
}





@Composable
fun YesNoQuestion(
    question: String,
    selected: String?,
    onAnswer: (String) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        Text(question, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(8.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf("Yes", "No").forEach { opt ->
                FilterChip(
                    selected = selected == opt,
                    onClick = { onAnswer(opt) },
                    label = { Text(opt) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF800080),   // dark blue when selected
                        containerColor = Color(0xFFFFFFFF),          // light gray when not selected
                        selectedLabelColor = Color.White,             // text color when selected
                        labelColor = Color.Black                      // text color when not selected
                    )
                )
            }
        }
    }
}

@Composable
fun YesNoMaybeQuestion(
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
                        selectedContainerColor = Color(0xFF800080),   // dark blue when selected
                        containerColor = Color(0xFFFFFFFF),          // light gray when not selected
                        selectedLabelColor = Color.White,             // text color when selected
                        labelColor = Color.Black                      // text color when not selected
                    )
                )

            }
        }
    }
}
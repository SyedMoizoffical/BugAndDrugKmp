package com.medical.buganddrug.ui.QuickIDConsult.Q1

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medical.buganddrug.data.model.QoestionsModel.Q1Model.NewsItem
import com.medical.buganddrug.ui.QuickIDConsult.topBar
import com.medical.buganddrug.ui.theme.cardDarkBackgroundColor
import com.medical.buganddrug.ui.theme.card_softBackgroundColor
import com.medical.buganddrug.ui.theme.text_black
import com.medical.buganddrug.util.ErrorAlertDialog
import com.medical.buganddrug.util.LoadingOverlay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun QuestionOneScreen(
    viewModel: QuestionViewModel,
    onBackClick: () -> Unit = {},
    patientType: String,
    onSubmit: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val qsofaResponse = viewModel.q1QSofaResponse
    var showQsofaDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }

    var qsofaSubmitted by rememberSaveable { mutableStateOf(false) }
    val isLoading by viewModel.loading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFFFFFFF),
                        Color(0xFFF3E5F5)
                    )
                )
            )
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.Transparent) {
            QsafaStepOneScreen(
                onQsofaSubmit = { an ->
                    scope.launch {
                        viewModel.submitQ1QSofa(
                            respiratoryRate = an["RR ≥ 22 breaths/min?"] ?: 0,
                            systolicBP = an["Systolic BP ≤ 100 mmHg?"] ?: 0,
                            gcsScore = an["Altered Mentation (GCS < 15)?"] ?: 0
                        )
                        dialogTitle="qSOFA"
                    }
                },
                onNewsSubmit = { an ->
                    scope.launch {
                        dialogTitle="NEWS2"

                        val spo2Key = when {
                            an.containsKey("SpO2 (If patient has hypercapnic respiratory failure)") ->
                                "SpO2 (If patient has hypercapnic respiratory failure)"
                            an.containsKey("SpO2 (On room air or supplemental O₂)") ->
                                "SpO2 (On room air or supplemental O₂)"
                            else -> null
                        }

                        viewModel.submitQ1News2(
                            Pulse = an["Pulse"]!!.toNewsItem(),
                            RoomAirOrSupplementalO2 = an["Room air or supplemental O₂"]!!.toNewsItem(),
                            RespiratoryRate = an["Respiratory rate"]!!.toNewsItem(),
                            SystolicBP = an["Systolic BP"]!!.toNewsItem(),
                            HypercapnicRespiratoryFailure = an["Hypercapnic respiratory failure"]!!.toNewsItem(),
                            Temperature = an["Temperature"]!!.toNewsItem(),
                            Spo2 = spo2Key!!.let { an[it]!!.toNewsItem() },
                            Consciousness = an["Consciousness"]!!.toNewsItem()
                        )
                    }
                },
                patientType = patientType,
                onBackClick = { onBackClick() },
                qsofaSubmitted = qsofaSubmitted
            )
        }

        if (isLoading) LoadingOverlay()
        if (errorMessage != null) {
            ErrorAlertDialog(
                errorMessage = errorMessage,
                onDismiss = { viewModel.clearError() }
            )
        }
        LaunchedEffect(qsofaResponse) {
            if (qsofaResponse != null) {
                qsofaSubmitted = true
                showQsofaDialog = true
            }
        }

        qsofaResponse?.let { response ->
            AlertDialog(
                onDismissRequest = { showQsofaDialog = false },
                title = { Text("$dialogTitle Result", style = MaterialTheme.typography.titleLarge) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {

                        // Score
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = Color(0xFF800080), fontWeight = FontWeight.SemiBold)) {
                                    append("Score: ")
                                }
                                withStyle(style = SpanStyle(color = Color.Black, fontWeight = FontWeight.SemiBold)) {
                                    append("${response.score}")
                                }
                            }
                        )

                        // Risk Level
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = Color(0xFF800080), fontWeight = FontWeight.SemiBold)) {
                                    append("Risk Level: ")
                                }
                                withStyle(style = SpanStyle(color = Color.Black)) {
                                    append("${response.riskLevel}")
                                }
                            }
                        )

                        Divider(Modifier.padding(vertical = 6.dp))

                        // Clinical Response heading
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = Color(0xFF800080), fontWeight = FontWeight.Medium)) {
                                    append("Clinical Response: ")
                                }
                                withStyle(style = SpanStyle(color = Color.Black)) {
                                    append(response.clinicalResponse ?: "-")
                                }
                            }
                        )
                    }

                },
                confirmButton = {
                    TextButton(onClick = {
                        showQsofaDialog = false
                        viewModel.clearQsofaResponse()
                    }) { Text("OK") }
                },
                containerColor = Color.White
            )
        }
    }
}

private fun Any.toNewsItem(): NewsItem {
    val m = this as Map<String, Any>
    return NewsItem(m["Name"] as String, m["Value"] as String, (m["Score"] as Number).toInt())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QsafaStepOneScreen(
    onQsofaSubmit: (answers: Map<String, Int>) -> Unit,
    onNewsSubmit: (answers: Map<String, Map<String, Any>>) -> Unit,
    onBackClick: () -> Unit = {},
    patientType: String,
    qsofaSubmitted: Boolean
) {
    val qsofaAnswers = remember { mutableStateMapOf<String, Int?>() }
    val newsValues = remember { mutableStateMapOf<String, Pair<String, Int>?>() }

    val qsofaQuestions = listOf(
        "RR ≥ 22 breaths/min?",
        "Altered Mentation (GCS < 15)?",
        "Systolic BP ≤ 100 mmHg?"
    )

    val newsQuestions = mapOf(
        "Respiratory rate (breaths/min)" to listOf("≤8" to 3, "9–11" to 1, "12–20" to 0, "21–24" to 2, "≥25" to 3),
        "Hypercapnic respiratory failure" to listOf("No" to 0, "Yes" to 1),
        "Room air or supplemental O₂" to listOf("Supplemental O₂" to 2, "Room air" to 0),
        "Temperature (°C)" to listOf("≤35.0" to 3, "35.1–36.0" to 1, "36.1–38.0" to 0, "38.1–39.0" to 1, "≥39.1" to 2),
        "Systolic BP (mmHg)" to listOf("≤90" to 3, "91–100" to 2, "101–110" to 1, "111–219" to 0, "≥220" to 3),
        "Pulse (bpm)" to listOf("≤40" to 3, "41–50" to 1, "51–90" to 0, "91–110" to 1, "111–130" to 2, "≥131" to 3),
        "Consciousness" to listOf(
            "Alert" to 0,
            "New-onset confusion (or disorientation/agitation), responds to voice, responds to pain, or unresponsive" to 3
        )
    )

    Scaffold(
        topBar = { topBar(onBackClick, "qSOFA + NEWS2", patientType = patientType) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            // QSOFA Card
            ElevatedCard(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.elevatedCardElevation(6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.98f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("qSOFA", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))

                    qsofaQuestions.forEach { question ->
                        Column(Modifier.padding(vertical = 8.dp)) {
                            Text(question, style = MaterialTheme.typography.bodyLarge)
                            Spacer(Modifier.height(6.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                FilterChip(
                                    selected = qsofaAnswers[question] == 1,
                                    onClick = { qsofaAnswers[question] = 1 },
                                    label = { Text("Yes") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFF800080),
                                        selectedLabelColor = Color.White
                                    )
                                )
                                FilterChip(
                                    selected = qsofaAnswers[question] == 0,
                                    onClick = { qsofaAnswers[question] = 0 },
                                    label = { Text("No") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFFE53935),
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }

                    val complete = qsofaAnswers.size == qsofaQuestions.size && qsofaAnswers.values.none { it == null }
                    Button(
                        onClick = { onQsofaSubmit(qsofaAnswers.filterValues { it != null }.mapValues { it.value!! }) },
                        enabled = complete && !qsofaSubmitted,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Submit qSOFA", fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // NEWS2 Card
            if (qsofaSubmitted) {
                ElevatedCard(
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.elevatedCardElevation(6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFF))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("NEWS2", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(12.dp))

                        val hypercapnicSelected = newsValues["Hypercapnic respiratory failure"]?.first
                        val spo2Question = when (hypercapnicSelected) {
                            "Yes" -> "SpO2 (If patient has hypercapnic respiratory failure)" to listOf(
                                "≤83%" to 3, "84–85%" to 2, "86-87%" to 1, "88-92%, ≥93% on room air" to 0,
                                "93-94% on supplemental O₂" to 1, "95-96% on supplemental O₂" to 2, "≥97% on supplemental O₂" to 3
                            )
                            "No" -> "SpO2 (On room air or supplemental O₂)" to listOf(
                                "≤91%" to 3, "92-93%" to 2, "94-95%" to 1, "≥96%" to 0
                            )
                            else -> null
                        }

                        newsQuestions.forEach { (question, options) ->
                            var expanded by remember { mutableStateOf(false) }
                            val selected = newsValues[question]

                            Column(Modifier.padding(vertical = 8.dp)) {
                                Text(question, style = MaterialTheme.typography.bodyLarge)
                                Spacer(Modifier.height(6.dp))
                                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                                    OutlinedTextField(
                                        value = selected?.first ?: "",
                                        onValueChange = {},
                                        readOnly = true,
                                        label = { Text("Select option") },
                                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White)
                                    )
                                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                        options.forEach { (label, score) ->
                                            DropdownMenuItem(
                                                text = { Text("$label (Score: $score)") },
                                                onClick = {
                                                    newsValues[question] = label to score
                                                    expanded = false
                                                    if (question == "Hypercapnic respiratory failure") {
                                                        newsValues.remove("SpO2 (If patient has hypercapnic respiratory failure)")
                                                        newsValues.remove("SpO2 (On room air or supplemental O₂)")
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            }

                            // Insert SpO₂ next
                            if (question == "Hypercapnic respiratory failure" && spo2Question != null) {
                                val (spo2Q, spo2Options) = spo2Question
                                var expandedSpO2 by remember { mutableStateOf(false) }
                                val selectedSpO2 = newsValues[spo2Q]
                                Column(Modifier.padding(vertical = 8.dp)) {
                                    Text(spo2Q, style = MaterialTheme.typography.bodyLarge)
                                    Spacer(Modifier.height(6.dp))
                                    ExposedDropdownMenuBox(expanded = expandedSpO2, onExpandedChange = { expandedSpO2 = it }) {
                                        OutlinedTextField(
                                            value = selectedSpO2?.first ?: "",
                                            onValueChange = {},
                                            readOnly = true,
                                            label = { Text("Select SpO₂") },
                                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedSpO2) },
                                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                                            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White)
                                        )
                                        ExposedDropdownMenu(expanded = expandedSpO2, onDismissRequest = { expandedSpO2 = false }) {
                                            spo2Options.forEach { (label, score) ->
                                                DropdownMenuItem(
                                                    text = { Text("$label (Score: $score)") },
                                                    onClick = {
                                                        newsValues[spo2Q] = label to score
                                                        expandedSpO2 = false
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        val requiredCount = newsQuestions.size + if (spo2Question != null) 1 else 0
                        val complete = newsValues.size == requiredCount && newsValues.values.all { it != null }

                        Button(
                            onClick = {
                                val formatted = newsValues.map { (question, pair) ->
                                    val (label, score) = pair ?: ("" to 0)
                                    val clean = question
                                        .removeSuffix(" (°C)")
                                        .removeSuffix(" (bpm)")
                                        .removeSuffix(" (mmHg)")
                                        .removeSuffix(" (breaths/min)")
                                    clean to mapOf("Name" to question, "Value" to label, "Score" to score)
                                }.toMap()
                              //  Log.d("COMBINED_SUBMIT", "NEWS Score: $formatted")
                                onNewsSubmit(formatted)
                            },
                            enabled = complete,
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Submit NEWS2", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewQsafaStepOneScreen() {
    MaterialTheme {
        val vm: QuestionViewModel = koinInject()

        QuestionOneScreen(viewModel = vm, patientType = "Inpatient")
    }
}

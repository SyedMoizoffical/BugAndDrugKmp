package com.medical.buganddrug.ui.clinicalSyndrome

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import buganddrug_multiplateform.composeapp.generated.resources.Res
import buganddrug_multiplateform.composeapp.generated.resources.back_arrow
import buganddrug_multiplateform.composeapp.generated.resources.info
import com.medical.buganddrug.ui.QuickIDConsult.topBar
import com.medical.buganddrug.util.ErrorAlertDialog
import com.medical.buganddrug.util.LoadingOverlay
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ClinicalSyndromeScreen(
    viewModel: ClinicalSyndromeViewModel,
    onBackClick: () -> Unit = {}
) {
    LaunchedEffect(Unit) { viewModel.getClinicalSyndromeData() }

    val response = viewModel.getSyndromeIdentificationData
    val isLoading by viewModel.loading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedSyndromeId by remember { mutableStateOf<Int?>(null) }
    var selectedDiseaseId by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            topBar(
                topic = "Syndrome Disease Finder",
                patientType = "Step-by-step selection",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                      Brush.verticalGradient(
                        listOf(
                            Color(0xFFFFFFFF),
                            Color(0xFFF3E5F5)   // Very soft lavender
                        )
                    )
                )
        ) {
            when {
                isLoading -> LoadingOverlay()

                errorMessage != null -> ErrorAlertDialog(
                    errorMessage = errorMessage,
                    onDismiss = { viewModel.clearError() }
                )

                response != null -> {
                    val syndromes = response.syndromes
                    val diseases = response.disease
                    val diseaseIdenticifationlists = response.diseaseIdenticifationlists

                    val syndromeList = syndromes
                        .distinctBy { it.syndromeId }
                        .map { it.syndromeName to it.syndromeId }

                    val filteredSyndromes = syndromeList.filter {
                        it.first.contains(searchQuery, ignoreCase = true)
                    }

                    val filteredDiseases = if (selectedSyndromeId != null) {
                        syndromes.filter { it.syndromeId == selectedSyndromeId }
                            .mapNotNull { syndrome ->
                                diseases.find { it.diseaseID == syndrome.diseaseID }.also {
                                }
                            }
                    } else emptyList()

                    val diseaseList = filteredDiseases.map { it.diseaseName to it.diseaseID }

//                    val selectedDiseaseSignlist = diseaseSignlist.find { it.diseaseId == selectedDiseaseId }
//                    val selectedSyndromeslist = syndromes.find { it.diseaseID == selectedDiseaseId }
//                    val selectedsyndromeTestslist = syndromeTests.find { it.diseaseID == selectedDiseaseId }
//                    val selectedEtilogicalAgents = etilogicalAgents.find { it.id == selectedDiseaseId }
                    val selecteddiseaseIdenticifationlists =
                        diseaseIdenticifationlists
                            .filter {
                                it.diseaseId == selectedDiseaseId &&
                                        it.localizationId == selectedSyndromeId
                            }
                            .distinctBy { it.symptomName }
                    val selectedSyndromeName = syndromeList.find { it.second == selectedSyndromeId }?.first
                    val selectedDiseaseName = diseases.find { it.diseaseID == selectedDiseaseId }?.diseaseName

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // 🧭 Elegant Dynamic Header
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            // Text takes remaining space
                            Text(
                                when {
                                    selectedSyndromeId == null -> "Syndromes"
                                    selectedDiseaseId == null -> selectedSyndromeName ?: ""
                                    else -> "${selectedSyndromeName ?: ""} → ${selectedDiseaseName ?: ""}"
                                },
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 22.sp,
                                    color = Color(0xFF800080)
                                ),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp) // Optional spacing from button
                            )

                            if (selectedSyndromeId != null) {
                                IconButton(
                                    onClick = {
                                        if (selectedDiseaseId != null) selectedDiseaseId = null
                                        else selectedSyndromeId = null
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.back_arrow),
                                        contentDescription = "Back",
                                        tint = Color(0xFF800080)
                                    )
                                }
                            }
                        }

                        // 🩺 Step 1: Syndrome Selection
                        if (selectedSyndromeId == null) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Search Syndrome") },
                                singleLine = true
                            )

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                items(filteredSyndromes.size) { index ->
                                    val item = filteredSyndromes[index]
                                    ElevatedCard(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 5.dp)
                                            .clickable {
                                                selectedSyndromeId = item.second
                                                selectedDiseaseId = null
                                            },
                                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color.White
                                        )
                                    ) {
                                        Text(
                                            text = item.first,
                                            modifier = Modifier.padding(16.dp),
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                fontWeight = FontWeight.Medium
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        // 🧬 Step 2: Disease Selection
                        AnimatedVisibility(
                            visible = selectedSyndromeId != null && selectedDiseaseId == null,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(top = 8.dp)
                            ) {
                                items(diseaseList.size) { index ->
                                    val item = diseaseList[index]
                                    ElevatedCard(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 5.dp)
                                            .clickable { selectedDiseaseId = item.second },
                                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White)
                                    ) {
                                        Text(
                                            text = item.first,
                                            modifier = Modifier.padding(16.dp),
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                fontWeight = FontWeight.Medium
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        // 💊 Step 3: Disease Details
                        AnimatedVisibility(
                            visible = selectedDiseaseId != null,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(top = 8.dp)
                            ) {
                                selecteddiseaseIdenticifationlists?.let { detail ->
                                    item {
                                        DetailCard(
                                            title = "Etiological Agents",
                                            content =  detail.map { item ->
                                                item.etiologicalAgent
                                            }
                                        )
                                    }
                                }

                                selecteddiseaseIdenticifationlists.let { list ->
                                    item {
                                        DetailCard(
                                            title = "Core Symptoms",
                                            content = list.map { item ->
                                                item.coreSymptoms
                                            }
                                        )
                                    }
                                }
                                selecteddiseaseIdenticifationlists.let { list ->
                                    item {
                                        DetailCard(
                                            title = "Optional Symptoms",
                                            content = list.map { item ->
                                                item.optionalSymptoms
                                            }
                                        )
                                    }
                                }
                                selecteddiseaseIdenticifationlists?.let { syndrome ->
                                    item {
                                        DetailCard(
                                            title = "Relevant Exposure",
                                            content = syndrome.map { item ->
                                                item.relevantExposure
                                            }
                                        )
                                    }
                                }
                                selecteddiseaseIdenticifationlists?.let { syndrome ->
                                    item {
                                        DetailCard(
                                            title = "Sign",
                                            content = syndrome.map { item ->
                                                item.signs
                                            }
                                        )
                                    }
                                }
                                selecteddiseaseIdenticifationlists?.let { syndrome ->
                                    item {
                                        DetailCard(
                                            title = "Diagnostic Tests",
                                            content = syndrome.map { item ->
                                               item.diagnosticTests
                                            }
                                        )
                                    }
                                }
                                selecteddiseaseIdenticifationlists?.let { syndrome ->
                                    item {
                                        DetailCard(
                                            title = "Treatment",
                                            content = syndrome.map { item ->
                                               item.treatment
                                            }
                                        )
                                    }
                                }
                                selecteddiseaseIdenticifationlists?.let { syndrome ->
                                    item {
                                        DetailCard(
                                            title = "Duration Of Treatment",
                                            content = syndrome.map { item ->
                                                item.duratioOfTreatment ?: ""
                                            }
                                        )
                                    }
                                }





                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailCard(
    title: String,
    content: List<String>?
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF800080)
                )
            )

            Spacer(Modifier.height(8.dp))
            val itemsToShow = when {
                content == null -> listOf("N/A")
                content.isEmpty() -> listOf("N/A")           // also good to handle empty list
                else -> content.map { it ?: "N/A" }          // ← key fix: replace null with "N/A"
            }
            itemsToShow.forEach { item ->
                Text(
                    text = "• $item",
                    color = Color(0xFF424242)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}


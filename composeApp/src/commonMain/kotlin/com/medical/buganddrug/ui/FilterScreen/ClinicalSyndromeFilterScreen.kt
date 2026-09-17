package com.medical.buganddrug.ui.FilterScreen

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape

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
import com.medical.buganddrug.ui.clinicalSyndrome.ClinicalSyndromeViewModel
import com.medical.buganddrug.util.ErrorAlertDialog
import com.medical.buganddrug.util.LoadingOverlay
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ClinicalSyndromeFilterScreen(
    viewModel: ClinicalSyndromeViewModel,
    onBackClick: () -> Unit = {},
    diseaseId: Int? = null,
    diseaseName: String? = null
) {
    LaunchedEffect(Unit) { viewModel.getClinicalSyndromeData() }

    val response = viewModel.getSyndromeIdentificationData
    val isLoading by viewModel.loading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedSyndromeId by remember { mutableStateOf<Int?>(null) }
    var selectedDiseaseId by remember { mutableStateOf<Int?>(null) }
    var passedDiseaseName by remember(diseaseName) { mutableStateOf<String?>(diseaseName) }

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
                        it.first!!.contains(searchQuery, ignoreCase = true)
                    }

                    val filteredDiseases = if (selectedSyndromeId != null) {
                        syndromes.filter { it.syndromeId == selectedSyndromeId }
                            .mapNotNull { syndrome ->
                                diseases.find { it.diseaseID == syndrome.diseaseID }.also {
                                }
                            }
                    } else emptyList()

                    val diseaseList = filteredDiseases.map { it.diseaseName to it.diseaseID }

                    val matchedDiseaseFromList = remember(response, passedDiseaseName, diseaseId) {
                        if (!passedDiseaseName.isNullOrBlank()) {
                            val nameToMatch = passedDiseaseName!!.trim()
                            diseases.find { it.diseaseName?.trim().equals(nameToMatch, ignoreCase = true) }
                                ?: diseases.find {
                                    val dName = it.diseaseName?.trim() ?: ""
                                    dName.isNotBlank() && (
                                        dName.contains(nameToMatch, ignoreCase = true) ||
                                        nameToMatch.contains(dName, ignoreCase = true)
                                    )
                                }
                        } else if (diseaseId != null && diseaseId != 0) {
                            diseases.find { it.diseaseID == diseaseId }
                        } else {
                            null
                        }
                    }

                    val effectiveDiseaseId = selectedDiseaseId
                        ?: matchedDiseaseFromList?.diseaseID
                        ?: (if (passedDiseaseName.isNullOrBlank() && diseaseId != null && diseaseId != 0) diseaseId else null)

                    val effectiveDiseaseName = selectedDiseaseId?.let { sId -> diseases.find { it.diseaseID == sId }?.diseaseName }
                        ?: matchedDiseaseFromList?.diseaseName
                        ?: passedDiseaseName
                        ?: (if (diseaseId != null && diseaseId != 0) diseases.find { it.diseaseID == diseaseId }?.diseaseName else null)

                    val autoSyndromeId = remember(response, effectiveDiseaseId) {
                        if (effectiveDiseaseId != null && effectiveDiseaseId != 0) {
                            syndromes.find { it.diseaseID == effectiveDiseaseId }?.syndromeId
                        } else null
                    }

                    val currentSyndromeId = selectedSyndromeId ?: autoSyndromeId
                    val selectedSyndromeName = syndromeList.find { it.second == currentSyndromeId }?.first

                    val selecteddiseaseIdenticifationlists = remember(
                        response,
                        effectiveDiseaseId,
                        effectiveDiseaseName,
                        selectedSyndromeId
                    ) {
                        diseaseIdenticifationlists.filter { item ->
                            val matchById = effectiveDiseaseId != null && effectiveDiseaseId != 0 && item.diseaseId == effectiveDiseaseId
                            val matchByName = !effectiveDiseaseName.isNullOrBlank() && (
                                item.disease?.trim().equals(effectiveDiseaseName.trim(), ignoreCase = true) ||
                                item.disease?.contains(effectiveDiseaseName.trim(), ignoreCase = true) == true ||
                                effectiveDiseaseName.trim().contains(item.disease?.trim() ?: "", ignoreCase = true)
                            )
                            val matchesDisease = if (effectiveDiseaseId != null && effectiveDiseaseId != 0) (matchById || matchByName) else matchByName
                            val matchesSyndrome = selectedSyndromeId == null || item.localizationId == selectedSyndromeId
                            matchesDisease && matchesSyndrome
                        }.distinctBy { it.symptomName }
                    }

                    val selectedDiseaseName = effectiveDiseaseName

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
                                    effectiveDiseaseId != null || !effectiveDiseaseName.isNullOrBlank() -> {
                                        if (selectedSyndromeName != null) {
                                            "${selectedSyndromeName} → ${effectiveDiseaseName ?: ""}"
                                        } else {
                                            effectiveDiseaseName ?: "Disease Details"
                                        }
                                    }
                                    selectedSyndromeId == null -> "Syndromes"
                                    else -> selectedSyndromeName ?: ""
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

//                            if (selectedSyndromeId != null || selectedDiseaseId != null || !passedDiseaseName.isNullOrBlank()) {
//                                IconButton(
//                                    onClick = {
//                                        if (selectedDiseaseId != null || !passedDiseaseName.isNullOrBlank()) {
//                                            selectedDiseaseId = null
//                                            passedDiseaseName = null
//                                        } else if (selectedSyndromeId != null) {
//                                            selectedSyndromeId = null
//                                        }
//                                    }
//                                ) {
//                                    Icon(
//                                        painter = painterResource(Res.drawable.back_arrow),
//                                        contentDescription = "Back",
//                                        tint = Color(0xFF800080)
//                                    )
//                                }
//                            }
                        }

                        // 🩺 Step 1: Syndrome Selection
                        if (selectedSyndromeId == null && effectiveDiseaseId == null && effectiveDiseaseName.isNullOrBlank()) {
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
                                            text = item.first!!,
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
                            visible = selectedSyndromeId != null && effectiveDiseaseId == null && effectiveDiseaseName.isNullOrBlank(),
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
                                            text = item.first!!,
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
                            visible = effectiveDiseaseId != null || !effectiveDiseaseName.isNullOrBlank(),
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(top = 8.dp)
                            ) {
                                item {
                                    DetailCard(
                                        title = "Etiological Agents",
                                        content = selecteddiseaseIdenticifationlists.map { item -> item.etiologicalAgent ?: "" },
                                        diseaseList = viewModel.allExtractedDiseases
                                    )
                                }

                                item {
                                    DetailCard(
                                        title = "Core Symptoms",
                                        content = selecteddiseaseIdenticifationlists.map { item -> item.coreSymptoms ?: "" },
                                        diseaseList = viewModel.allExtractedDiseases
                                    )
                                }

                                item {
                                    DetailCard(
                                        title = "Optional Symptoms",
                                        content = selecteddiseaseIdenticifationlists.map { item -> item.optionalSymptoms ?: "" },
                                        diseaseList = viewModel.allExtractedDiseases
                                    )
                                }

                                item {
                                    DetailCard(
                                        title = "Relevant Exposure",
                                        content = selecteddiseaseIdenticifationlists.map { item -> item.relevantExposure ?: "" },
                                        diseaseList = viewModel.allExtractedDiseases
                                    )
                                }

                                item {
                                    DetailCard(
                                        title = "Sign",
                                        content = selecteddiseaseIdenticifationlists.map { item -> item.signs ?: "" },
                                        diseaseList = viewModel.allExtractedDiseases
                                    )
                                }

                                item {
                                    DetailCard(
                                        title = "Diagnostic Tests",
                                        content = selecteddiseaseIdenticifationlists.map { item -> item.diagnosticTests ?: "" },
                                        diseaseList = viewModel.allExtractedDiseases
                                    )
                                }

                                item {
                                    DetailCard(
                                        title = "Treatment",
                                        content = selecteddiseaseIdenticifationlists.map { item -> item.treatment ?: "" },
                                        diseaseList = viewModel.allExtractedDiseases
                                    )
                                }

                                item {
                                    DetailCard(
                                        title = "Duration Of Treatment",
                                        content = selecteddiseaseIdenticifationlists.map { item -> item.duratioOfTreatment ?: "" },
                                        diseaseList = viewModel.allExtractedDiseases
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

@Composable
fun DetailCard(
    title: String,
    content: List<String>?,
    diseaseList: List<com.medical.buganddrug.data.model.LocalStorageDatamodel.DiseaseItem> = emptyList()
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(18.dp)
                        .background(Color(0xFF800080), RoundedCornerShape(2.dp))
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF800080)
                    )
                )
            }

            Spacer(Modifier.height(10.dp))
            val itemsToShow = when {
                content == null -> listOf("N/A")
                content.isEmpty() -> listOf("N/A")
                else -> content.map { it ?: "N/A" }
            }
            itemsToShow.forEach { item ->
                Row(verticalAlignment = Alignment.Top) {
                    Text("• ", color = Color(0xFF616161), fontWeight = FontWeight.Bold)
                    if (item == "N/A") {
                        Text(
                            text = "N/A",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFF9E9E9E),
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        )
                    } else {
                        com.medical.buganddrug.util.ClickableDiseaseText(
                            text = item,
                            diseaseList = diseaseList,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFF212121),
                                lineHeight = 22.sp
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}



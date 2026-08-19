package com.medical.buganddrug.ui.clinicalSyndrome

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
                                singleLine = true,
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF800080),
                                    unfocusedBorderColor = Color(0xFFE0E0E0),
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White
                                )
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
                                        shape = RoundedCornerShape(14.dp),
                                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color.White
                                        )
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(16.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .width(4.dp)
                                                    .height(20.dp)
                                                    .background(Color(0xFF800080), RoundedCornerShape(2.dp))
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                text = item.first!!,
                                                style = MaterialTheme.typography.bodyLarge.copy(
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Color(0xFF1B2B5D)
                                                )
                                            )
                                        }
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
                                        shape = RoundedCornerShape(14.dp),
                                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(16.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .width(4.dp)
                                                    .height(20.dp)
                                                    .background(Color(0xFF800080), RoundedCornerShape(2.dp))
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                text = item.first!!,
                                                style = MaterialTheme.typography.bodyLarge.copy(
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Color(0xFF1B2B5D)
                                                )
                                            )
                                        }
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
                                            content = detail.map { item -> item.etiologicalAgent!! },
                                            diseaseList = viewModel.allExtractedDiseases
                                        )
                                    }
                                }

                                selecteddiseaseIdenticifationlists?.let { list ->
                                    item {
                                        DetailCard(
                                            title = "Core Symptoms",
                                            content = list.map { item -> item.coreSymptoms!! },
                                            diseaseList = viewModel.allExtractedDiseases
                                        )
                                    }
                                }

                                selecteddiseaseIdenticifationlists?.let { list ->
                                    item {
                                        DetailCard(
                                            title = "Optional Symptoms",
                                            content = list.map { item -> item.optionalSymptoms!! },
                                            diseaseList = viewModel.allExtractedDiseases
                                        )
                                    }
                                }

                                selecteddiseaseIdenticifationlists?.let { syndrome ->
                                    item {
                                        DetailCard(
                                            title = "Relevant Exposure",
                                            content = syndrome.map { item -> item.relevantExposure!! },
                                            diseaseList = viewModel.allExtractedDiseases
                                        )
                                    }
                                }

                                selecteddiseaseIdenticifationlists?.let { syndrome ->
                                    item {
                                        DetailCard(
                                            title = "Sign",
                                            content = syndrome.map { item -> item.signs!! },
                                            diseaseList = viewModel.allExtractedDiseases
                                        )
                                    }
                                }

                                selecteddiseaseIdenticifationlists?.let { syndrome ->
                                    item {
                                        DetailCard(
                                            title = "Diagnostic Tests",
                                            content = syndrome.map { item -> item.diagnosticTests!! },
                                            diseaseList = viewModel.allExtractedDiseases
                                        )
                                    }
                                }

                                selecteddiseaseIdenticifationlists?.let { syndrome ->
                                    item {
                                        DetailCard(
                                            title = "Treatment",
                                            content = syndrome.map { item -> item.treatment!! },
                                            diseaseList = viewModel.allExtractedDiseases
                                        )
                                    }
                                }

                                selecteddiseaseIdenticifationlists?.let { syndrome ->
                                    item {
                                        DetailCard(
                                            title = "Duration Of Treatment",
                                            content = syndrome.map { item -> item.duratioOfTreatment ?: "" },
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
}

@Composable
private fun DetailCard(
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

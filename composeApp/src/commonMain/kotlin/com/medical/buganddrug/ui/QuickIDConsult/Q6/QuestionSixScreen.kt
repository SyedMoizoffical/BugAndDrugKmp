package com.medical.buganddrug.ui.QuickIDConsult.Q5

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import buganddrug_multiplateform.composeapp.generated.resources.Res
import buganddrug_multiplateform.composeapp.generated.resources.info

import com.medical.buganddrug.data.model.QoestionsModel.Q5Model.AntibioticDose
import com.medical.buganddrug.ui.QuickIDConsult.Q3.SingleSelectSearchableSpinnerDialog
import com.medical.buganddrug.ui.QuickIDConsult.topBar
import com.medical.buganddrug.ui.theme.cardLightBackgroundColor
import com.medical.buganddrug.util.ErrorAlertDialog
import com.medical.buganddrug.util.LoadingOverlay
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

@Composable
fun QuestionSixScreen(
    viewModel: QuestionSixViewModel = koinInject(),
    onSubmit: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var selectedAntibiotic by remember { mutableStateOf<String?>(null) }
    var selectedAntibioticData by remember { mutableStateOf<List<AntibioticDose>>(emptyList()) }

    val isLoading by viewModel.loading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getAntibiotic()
    }

    Scaffold(
        topBar = {
            topBar(
                topic = "Antimicrobials",
                patientType = "Antimicrobial Agent",
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                val antibioticList = viewModel.antibioticDoses
                    .map { it.antibioticName }
                    .toSet()
                    .sorted()



                // 🔽 Dropdown (no card)
                Text(
                    text = "Select Antimicrobial Agent",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                SingleSelectSearchableSpinnerDialog(
                    label = "Select Antimicrobial Agent",
                    items = antibioticList.map { it to it },
                    itemLabel = { it.first },
                    selectedItem = selectedAntibiotic,
                    onItemSelected = { selected ->
                        selectedAntibiotic = selected?.second
                        selectedAntibioticData = viewModel.antibioticDoses.filter { data ->
                            data.antibioticName == selectedAntibiotic
                        }
                    }
                )

                // 💊 Antibiotic Detail / Placeholder
                if (selectedAntibioticData.isNotEmpty()) {
                    AntibioticDetailCard(selectedAntibiotic!!, selectedAntibioticData)
                } else {
                    EmptyPlaceholderCard()
                }
            }

            if (isLoading) LoadingOverlay()

            if (errorMessage != null) {
                ErrorAlertDialog(
                    errorMessage = errorMessage ?: "",
                    onDismiss = { viewModel.clearError() }
                )
            }
        }
    }
}

@Composable
private fun AntibioticDetailCard(selectedAntibiotic: String, data: List<AntibioticDose>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                shape = RoundedCornerShape(18.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.cardLightBackgroundColor)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.info),
                    contentDescription = "Info Icon",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Dose Details for $selectedAntibiotic",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }

            Spacer(Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                DetailSection("Antibiotic Class", data.map { it.antibioticClass }.distinct())
                DetailSection("WHO Aware Category", data.map { it.whoawareCategory?:"" }.distinct())
                DetailSection("Indications", data.map { it.indications }.distinct())

                DetailSection("Standard Dosing", data.map { it.standardDose }.distinct())
                Text(
                    text = "Creatinine Clearance Dose Adjustment",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )

                DoseAdjustmentTable(data)
                DetailSection("Preferred Against", data.map { it.preferredAgainst }.distinct())
                DetailSection("Pregnancy Class", data.map { it.pregnancyClass }.distinct())
                DetailSection("Lactation Class", data.map { it.lactationClass }.distinct())
                DetailSection("Drug Interactions", data.map { it.drugInteractions }.distinct())



            }

        }
    }
}
@Composable
fun DoseAdjustmentTable(data: List<AntibioticDose>) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                RoundedCornerShape(12.dp)
            )
    ) {
        // Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Creatinine Cl",
                modifier = Modifier.weight(1f).padding(start = 12.dp),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Adjusted Dose",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Table Rows
        data.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = item.creatinineClearanceRange,
                    modifier = Modifier.weight(1f).padding(start = 12.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = item.adjustedDosing,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        }
    }
}

@Composable
private fun EmptyPlaceholderCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                shape = RoundedCornerShape(18.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.info),
                contentDescription = "Placeholder Icon",
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                modifier = Modifier.size(48.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "No Antimicrobial Agent selected yet",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Please select an Antimicrobial Agent from the list above to view its dose details.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DetailSection(title: String, values: List<String>) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(4.dp))
        values.forEach { value ->
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
        Divider(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )
    }
}

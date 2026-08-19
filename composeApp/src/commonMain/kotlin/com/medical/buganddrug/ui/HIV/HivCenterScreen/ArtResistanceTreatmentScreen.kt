package com.medical.buganddrug.ui.HIV.HivCenterScreen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import buganddrug_multiplateform.composeapp.generated.resources.ExpandLess
import buganddrug_multiplateform.composeapp.generated.resources.Res
import buganddrug_multiplateform.composeapp.generated.resources.arrow_drop_down
import org.jetbrains.compose.resources.painterResource
import kotlinx.serialization.Serializable

@Serializable
data class ArtResistanceEntry(
    val drugClass: String,
    val drug: String,
    val resistanceGene: String,
    val resistancePattern: String,
    val recommendedTreatment: String
)


@Composable
fun ArtResistanceTreatmentScreen(onBackClick: () -> Unit = {}) {

    val entries = remember {
        listOf(
            ArtResistanceEntry(
                drugClass = "NRTI",
                drug = "Zidovudine (AZT)",
                resistanceGene = "Thyamdine Analoge Mutations (TAMs)",
                resistancePattern = "High-level NRTI resistance",
                recommendedTreatment = "Switch AZT to TDF and continue  3TC + DTG"
            ),
            ArtResistanceEntry(
                drugClass = "NRTI",
                drug = "Lamivudine (3TC)",
                resistanceGene = "M184V",
                resistancePattern = "High-level resistance to 3TC/FTC but AZT and TDF sensitivity increased. It delays AZT resistance",
                recommendedTreatment = "Continue 3TC"
            ),
            ArtResistanceEntry(
                drugClass = "NRTI",
                drug = "Tenofovir (TDF)",
                resistanceGene = "K65R",
                resistancePattern = "Reduced TDF/ABC activity. Increase AZT sensitivity",
                recommendedTreatment = "Switch TDF to AZT and continue  3TC + DTG"
            ),
            ArtResistanceEntry(
                drugClass = "NNRTI",
                drug = "Efavirenz (EFV)",
                resistanceGene = "K103N",
                resistancePattern = "High-level EFV/NVP resistance",
                recommendedTreatment = "Switch to DTG-based regimen"
            ),
            ArtResistanceEntry(
                drugClass = "NNRTI",
                drug = "Nevirapine (NVP)",
                resistanceGene = "Y181C",
                resistancePattern = "Cross-resistance to NNRTIs",
                recommendedTreatment = "Switch to DTG or boosted PI"
            ),
            ArtResistanceEntry(
                drugClass = "INSTI",
                drug = "Dolutegravir (DTG)",
                resistanceGene = "Q148",
                resistancePattern = "Resistance to all INSTI",
                recommendedTreatment = "Switch DTG to ATV/r"
            ),
            ArtResistanceEntry(
                drugClass = "INSTI",
                drug = "Raltegravir (RAL)",
                resistanceGene = "N155, Q148, Y143",
                resistancePattern = "High-level INSTI resistance",
                recommendedTreatment = "Switch to boosted PI regimen"
            )
        )
    }

    Scaffold(
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "11. ART Resistance and Treatment Option",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(Res.drawable.arrow_drop_down),
                        modifier = Modifier.size(24.dp) // actual icon size
,
                                contentDescription = "Back"
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(entries) { entry ->
                ArtResistanceCard(entry)
            }
        }
    }
}
@Composable
private fun ArtResistanceCard(entry: ArtResistanceEntry) {
    var expanded by remember { mutableStateOf(false) }

    val (badgeBg, badgeText) = when (entry.drugClass.uppercase()) {
        "NRTI" -> Pair(Color(0xFFF3E5F5), Color(0xFF800080))
        "NNRTI" -> Pair(Color(0xFFE0F2FE), Color(0xFF0369A1))
        "INSTI" -> Pair(Color(0xFFE0F2FE), Color(0xFF0D9488))
        else -> Pair(Color(0xFFF1F5F9), Color(0xFF475569))
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = badgeBg,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(
                                text = entry.drugClass,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = badgeText
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Text(
                            text = entry.drug,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = entry.resistanceGene,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF800080)
                        )
                    )
                }

                Icon(
                    painter = painterResource(
                        if (expanded)
                            Res.drawable.ExpandLess
                        else
                            Res.drawable.arrow_drop_down
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF800080)
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {

                    HorizontalDivider(color = Color(0xFFE2E8F0))
                    Spacer(modifier = Modifier.height(12.dp))

                    DetailRow("Common Resistance Pattern", entry.resistancePattern)
                    Spacer(modifier = Modifier.height(10.dp))
                    DetailRow("Recommended Treatment Option (WHO-based)", entry.recommendedTreatment)
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF800080)
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        com.medical.buganddrug.util.ClickableDiseaseText(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color(0xFF1E293B),
                lineHeight = 22.sp
            )
        )
    }
}

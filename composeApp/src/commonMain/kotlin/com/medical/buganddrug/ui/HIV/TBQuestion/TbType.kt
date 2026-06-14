package com.medical.buganddrug.ui.HIV.TBQuestion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import buganddrug_multiplateform.composeapp.generated.resources.Res
import buganddrug_multiplateform.composeapp.generated.resources.arrow_drop_down
import buganddrug_multiplateform.composeapp.generated.resources.info
import com.medical.buganddrug.ui.QuickIDConsult.Q4.QuestionFourViewModel
import com.medical.buganddrug.ui.QuickIDConsult.Q4.SingleSelectSearchableSpinnerDialog
import com.medical.buganddrug.ui.QuickIDConsult.topBar
import org.jetbrains.compose.resources.painterResource

// ----------------------------------------------------------------------
// UPDATED MODEL for TB Types
// ----------------------------------------------------------------------
data class TbType(
    val tbType: String,                    // Heading / Main Type
    val definition: String,
    val drugResistancePattern: String,
    val diagnosticBasis: String,
    val recommendedTreatment: String,      // Pakistan NTP / WHO
    val referralRequirement: String
)

// ----------------------------------------------------------------------
// MOCK DATA – TB Types based on Resistance Pattern
// ----------------------------------------------------------------------
val tbTypesList = listOf(
    TbType(
        tbType = "Drug-sensitive TB (DS-TB)",
        definition = "TB sensitive to all first-line drugs including rifampicin and isoniazid",
        drugResistancePattern = "No resistance detected",
        diagnosticBasis = "Xpert MTB/RIF: RIF sensitive ± DST",
        recommendedTreatment = "Standard first-line regimen: 2 months HRZE + 4 months HR",
        referralRequirement = "No (managed at basic TB unit)"
    ),
    TbType(
        tbType = "Mono-resistant TB",
        definition = "Resistance to one first-line anti-TB drug only",
        drugResistancePattern = "Isoniazid or Ethambutol or Pyrazinamide resistance",
        diagnosticBasis = "Phenotypic or molecular DST",
        recommendedTreatment = "Modified regimen excluding resistant drug; specialist input required",
        referralRequirement = "Yes"
    ),
    TbType(
        tbType = "Poly-resistant TB",
        definition = "Resistance to more than one first-line drug excluding both INH and RIF together",
        drugResistancePattern = "Multiple first-line drugs resistant (not INH+RIF)",
        diagnosticBasis = "Phenotypic or molecular DST",
        recommendedTreatment = "Individualized regimen based on DST",
        referralRequirement = "Yes"
    ),
    TbType(
        tbType = "Rifampicin-resistant TB (RR-TB)",
        definition = "Resistance to rifampicin with or without resistance to other drugs",
        drugResistancePattern = "RIF resistance detected",
        diagnosticBasis = "Xpert MTB/RIF or DST",
        recommendedTreatment = "Treat as MDR-TB using all-oral second-line regimen",
        referralRequirement = "Mandatory DR-TB center referral"
    ),
    TbType(
        tbType = "Multidrug-resistant TB (MDR-TB)",
        definition = "Resistance to at least isoniazid and rifampicin",
        drugResistancePattern = "INH + RIF resistance",
        diagnosticBasis = "Phenotypic or molecular DST",
        recommendedTreatment = "All-oral longer or shorter MDR-TB regimen (bedaquiline-based)",
        referralRequirement = "Mandatory DR-TB center referral"
    ),
    TbType(
        tbType = "Pre-XDR TB",
        definition = "MDR-TB with additional resistance to any fluoroquinolone",
        drugResistancePattern = "INH + RIF + FQ resistance",
        diagnosticBasis = "Second-line DST",
        recommendedTreatment = "Longer individualized all-oral regimen excluding fluoroquinolones",
        referralRequirement = "Mandatory DR-TB expert management"
    ),
    TbType(
        tbType = "XDR-TB",
        definition = "MDR-TB with resistance to fluoroquinolone and bedaquiline or linezolid",
        drugResistancePattern = "INH + RIF + FQ + Group A drug resistance",
        diagnosticBasis = "Comprehensive DST",
        recommendedTreatment = "Highly individualized regimen under expert DR-TB committee",
        referralRequirement = "Mandatory national-level DR-TB care"
    )
)

// ----------------------------------------------------------------------
// MAIN SCREEN
// ----------------------------------------------------------------------
@Composable
fun TbResistanceTypesScreen(
    onBackClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Title first, takes available space
                Text(
                    text = "3. Types of TB based on Resistance Pattern",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp), // symmetric padding on left & right
                    textAlign = TextAlign.Start
                )

                // IconButton on the right
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                       painter = painterResource(Res.drawable.arrow_drop_down),
                        contentDescription = "Back to questions",
                        modifier = Modifier
                            .size(28.dp)
                            .rotate(if (true) 180f else 0f), // 180° = up arrow for "collapse"
                        tint = Color(0xFF800080)
                    )
                }
            }
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
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {


                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(tbTypesList) { index, item ->

                        TbQuestionItem(
                            title = item.tbType,
                            isSelected = selectedIndex == index,
                            onClick = {
                                selectedIndex = if (selectedIndex == index) null else index
                            }
                        )

                        if (selectedIndex == index) {
                            TbDetailCard(item)
                        }
                    }
                }


                Spacer(modifier = Modifier.height(20.dp))

                val selected = selectedIndex?.let { tbTypesList[it] }
                if (selected != null) {
                    TbDetailCard(selected)
                }




            }
        }
    }
}

// ----------------------------------------------------------------------
// DETAILS CARD
// ----------------------------------------------------------------------
@Composable
fun TbDetailCard(data: TbType) {
    Card(
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFD8E7FF))
                    .padding(12.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.info),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = data.tbType,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Content rows
            InfoRow("Definition", data.definition)
            InfoRow("Drug Resistance Pattern", data.drugResistancePattern)
            InfoRow("Diagnostic Basis", data.diagnosticBasis)
            InfoRow("Recommended Treatment (Pakistan NTP / WHO)", data.recommendedTreatment)
            InfoRow("Referral Requirement", data.referralRequirement)
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column {
        Text(
            label,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(value, color = MaterialTheme.colorScheme.onSurface)
        Divider(
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 0.5.dp,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}

// ----------------------------------------------------------------------
// EMPTY MESSAGE
// ----------------------------------------------------------------------

@Composable
fun TbQuestionItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                Color(0xFFE3EEFF)
            else
                Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(Res.drawable.arrow_drop_down),                contentDescription = null,
                modifier = Modifier.rotate(if (isSelected) 180f else 0f),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

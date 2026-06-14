package com.medical.buganddrug.ui.HIV.TBQuestion

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.sp
import buganddrug_multiplateform.composeapp.generated.resources.ExpandLess
import buganddrug_multiplateform.composeapp.generated.resources.Res
import buganddrug_multiplateform.composeapp.generated.resources.arrow_drop_down
import com.medical.buganddrug.ui.onboarding.PatientTypeSelectionScreen
import org.jetbrains.compose.resources.painterResource

// ----------------------------------------------------------------------
// MODELS
// ----------------------------------------------------------------------
data class TbQuestion(
    val title: String,
    val id: Int = 0,
    val answer: String = ""
)

data class WeightBandRow(
    val weight: String,
    val intensive: String,
    val continuation: String
)

data class DrugDoseRow(
    val drug: String,
    val adultDose: String,
    val pediatricDose: String
)

// ----------------------------------------------------------------------
// DATA
// ----------------------------------------------------------------------
val tbQuestions = listOf(

    TbQuestion(id=1,
        title = "Q1. Anti-tuberculous Drugs",
        answer = """
First-line anti-TB drugs (for DS-TB)
These drugs are used for drug-sensitive tuberculosis and form the standard 6-month regimen.
Isoniazid (H)
Rifampicin (R)
Pyrazinamide (Z)
Ethambutol (E)
• Streptomycin is no longer recommended routinely as first-line

Second-line anti-TB drugs (for DR-TB)
Group A:
Levofloxacin or Moxifloxacin
Bedaquiline
Linezolid

Group B:
Clofazimine
Cycloserine / Terizidone

Group C:
Ethambutol
Delamanid
Pyrazinamide
Imipenem–cilastatin or Meropenem (with clavulanate)
Amikacin or Streptomycin
Ethionamide or Prothionamide
p-Aminosalicylic acid (PAS)
""".trimIndent()
    ),

    TbQuestion(id=2,
        title = "Q2. Fixed Drug Combinations (FDC) Available in Pakistan",
        answer = """
Adults

4-drug adult FDC (RHZE)
Rifampicin 150 mg
Isoniazid 75 mg
Pyrazinamide 400 mg
Ethambutol 275 mg

3-drug adult FDC (RHE)
Rifampicin 150 mg
Isoniazid 75 mg
Ethambutol 275 mg

2-drug adult FDC (RH)
Rifampicin 150 mg
Isoniazid 75 mg

Paediatric FDCs

3-drug paediatric FDC (RHZ)
Rifampicin 75 mg
Isoniazid 50 mg
Pyrazinamide 150 mg

2-drug paediatric FDC (RH)
Rifampicin 75 mg
Isoniazid 50 mg
""".trimIndent()
    ),

    TbQuestion(id = 3,
        title = "Q3. Anti-tuberculous Therapy Weight Based Dosing",
    ),

    TbQuestion(id = 4,
        title = "Q4. Drug-Sensitive TB (DS-TB) Treatment",
        answer = """
Standard 6-month regimen:
2 months HRZE
4 months HR

9–12 months:
Pericardial TB
Disseminated TB
Bone/joint TB
Spinal TB
CNS TB
""".trimIndent()
    ),

    TbQuestion(id = 5,
        title = "Q5. Drug-Resistant (DR-TB) Treatment",
        answer = """
All-oral shorter regimens prioritized (2024 guidelines)

Standard MDR (FQ susceptible):
Bedaquiline + Pretomanid + Linezolid + Moxifloxacin (~6 months)

Monitoring:
Monthly sputum testing
Clinical assessment
ECG, LFTs, CBC, renal function
""".trimIndent()
    )
)

// ----------------------------------------------------------------------
// TABLE DATA (Q3)
// ----------------------------------------------------------------------
val weightBandTable = listOf(
    WeightBandRow("30–39 kg", "2 tablets/day", "2 tablets/day"),
    WeightBandRow("40–54 kg", "3 tablets/day", "3 tablets/day"),
    WeightBandRow("55+ kg", "4 tablets/day", "4 tablets/day")
)

val drugDoseTable = listOf(
    DrugDoseRow("Isoniazid (H)", "5 mg/kg/day (max 300 mg)", "10 mg/kg/day (max 300 mg)"),
    DrugDoseRow("Rifampicin (R)", "10 mg/kg/day (max 600 mg)", "15 mg/kg/day (max 600 mg)"),
    DrugDoseRow("Pyrazinamide (Z)", "20–25 mg/kg/day", "30–40 mg/kg/day"),
    DrugDoseRow("Ethambutol (E)", "15–20 mg/kg/day", "20 mg/kg/day"),
    DrugDoseRow("Streptomycin", "15 mg/kg/day (max 1 g)", "15–20 mg/kg/day"),
    DrugDoseRow("Levofloxacin", "750–1000 mg/day (~15 mg/kg)", "15–20 mg/kg/day"),
    DrugDoseRow("Moxifloxacin", "400 mg/day", "Not routinely recommended"),
    DrugDoseRow("Linezolid", "600 mg/day", "10 mg/kg/dose"),
    DrugDoseRow("Bedaquiline", "400 mg daily x 2 weeks, then 200 mg thrice weekly", "Specialist use only"),
    DrugDoseRow("Clofazimine", "100 mg/day", "2–5 mg/kg/day")
)

// ----------------------------------------------------------------------
// SCREEN
// ----------------------------------------------------------------------
@Composable
fun TbTreatmentScreen(    onBackClick: () -> Unit = {}
) {

    var expandedIndex by remember { mutableStateOf<Int?>(null) }

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
                    text = "4. TB Treatment",
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
            }        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf( Color.White,Color(0xFFF3E5F5))
                    )
                )
                .padding(padding)
        ) {

            LazyColumn(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                itemsIndexed(tbQuestions) { index, item ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                expandedIndex = if (expandedIndex == index) null else index
                            },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                    ) {

                        Column(
                            modifier = Modifier
                                .padding(horizontal = 18.dp, vertical = 16.dp)
                        ) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )

                                Icon(
                                    painter = painterResource(Res.drawable.arrow_drop_down),                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .size(26.dp)
                                        .rotate(if (expandedIndex == index) 180f else 0f)
                                )
                            }

                            AnimatedVisibility(visible = expandedIndex == index) {

                                Column {
                                    Spacer(modifier = Modifier.height(12.dp))

                                    Divider(
                                        thickness = 1.dp,
                                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    when (item.id) {
                                        3 -> AntiTbTherapyTableScreen()
                                        4 -> DrugResistantTBGuidelinesScreen()
                                        else -> Text(
                                            text = item.answer,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            lineHeight = 20.sp
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

// ----------------------------------------------------------------------
// Q3 TABLES
// ----------------------------------------------------------------------


// -----------------------------
// Data Model for Drug Dose Table
// -----------------------------
data class DrugDose(
    val drugName: String,
    val adultDose: String,
    val pediatricDose: String
)

// -----------------------------
// Sample Data
// -----------------------------
val drugDoseList = listOf(
    DrugDose("Isoniazid (H)", "5 mg/kg/day (max 300 mg)", "10 mg/kg/day (max 300 mg)"),
    DrugDose("Rifampicin (R)", "10 mg/kg/day (max 600 mg)", "15 mg/kg/day (max 600 mg)"),
    DrugDose("Pyrazinamide (Z)", "20–25 mg/kg/day", "30–40 mg/kg/day"),
    DrugDose("Ethambutol (E)", "15–20 mg/kg/day", "20 mg/kg/day"),
    DrugDose("Streptomycin", "15 mg/kg/day (max 1 g)", "15–20 mg/kg/day"),
    DrugDose("Levofloxacin", "750–1000 mg/day (~15 mg/kg)", "15–20 mg/kg/day"),
    DrugDose("Moxifloxacin", "400 mg/day", "Not routinely recommended"),
    DrugDose("Linezolid", "600 mg/day", "10 mg/kg/dose"),
    DrugDose("Bedaquiline", "400 mg daily x 2 weeks, then 200 mg thrice weekly", "Specialist use only"),
    DrugDose("Clofazimine", "100 mg/day", "2–5 mg/kg/day")
)

// -----------------------------
// Main Table Screen
// -----------------------------
@Composable
fun AntiTbTherapyTableScreen(
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3E5F5))
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            "Anti-tuberculous Therapy Weight Based Dosing",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF800080)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Adult Weight Bands (Daily FDC Tablets)",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF800080)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Weight Band Table
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                TableRow(header = true, columns = listOf("Weight band (kg)", "Intensive phase (HRZE)", "Continuation phase (HR)"))
                TableRow(columns = listOf("30–39 kg", "2 tablets/day", "2 tablets/day"))
                TableRow(columns = listOf("40–54 kg", "3 tablets/day", "3 tablets/day"))
                TableRow(columns = listOf("55+ kg", "4 tablets/day", "4 tablets/day"))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "(If >70 kg, some programs use 5 tablets/day, but Pakistan NTP lists up to “55 & above” as 4 tablets/day in standard adult dosing tables.)",
            fontSize = 14.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Individual Drug Dose",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF800080)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Drug Dose Table
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                TableRow(header = true, columns = listOf("Drug name", "Adult Wt-based dose", "Pediatric Wt-based dose"))
                drugDoseList.forEachIndexed { index, drug ->
                    TableRow(
                        columns = listOf(drug.drugName, drug.adultDose, drug.pediatricDose),
                        backgroundColor = if (index % 2 == 0) Color(0xFFF4F6FF) else Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// -----------------------------
// Table Row Composable
// -----------------------------
@Composable
fun TableRow(
    columns: List<String>,
    header: Boolean = false,
    backgroundColor: Color = Color(0xFFE3EEFF)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (header) Color(0xFFD8E7FF) else backgroundColor)
            .border(1.dp, Color.White)
    ) {
        columns.forEach { column ->
            Text(
                text = column,
                fontWeight = if (header) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp),
                color = Color.Black,
                fontSize = 10.sp
            )
        }
    }
}
//Question  4
@Composable
fun DrugResistantTBGuidelinesScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Header with accent color
        Text(
            text = "Drug-Resistant TB Treatment",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Text(
            text = "2024–2025 WHO guidelines prioritize shorter, all-oral regimens for MDR/RR-TB and XDR-TB, offering higher success rates with fewer adverse events.",
            style = MaterialTheme.typography.bodyLarge.copy(
                lineHeight = 28.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Section 1: Confirm Resistance Profile
        ExpandableSection(title = "1. Confirm Resistance Profile") {
            NumberedListItem(1, "Start with rapid molecular testing (GeneXpert MTB/RIF Ultra or similar) to detect rifampicin resistance.")
            BulletPoint("• If rifampicin-resistant, proceed to Line Probe Assay (LPA) or Xpert MTB/XDR for fluoroquinolone and second-line drug resistance.")
            NumberedListItem(3, "Send culture for full phenotypic drug susceptibility testing (DST) when possible.")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section 2: Interpret Resistance Pattern
        ExpandableSection(title = "2. Interpret Resistance Pattern") {
            NumberedListItem(1, "RR-TB / MDR-TB: Resistance to at least rifampicin (usually also isoniazid).")
            NumberedListItem(2, "Fluoroquinolone resistance (pre-XDR/XDR): Avoid FQ-based regimens and add additional agents.")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section 3: Recommended Regimens (with modern table)
        SectionTitle("3. Recommended Regimens (WHO 2024–2025)")

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
        ) {
            Column(
            ) {
                // Header Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TableHeaderText("Type of TB", weight = 1.2f)
                    TableHeaderText("Regimen", weight = 1.3f)
                    TableHeaderText("Key Drugs", weight = 2f)
                    TableHeaderText("Duration", weight = 1f)
                }

                Divider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)

                // Rows with alternating background
                listOf(
                    Triple("MDR/RR-TB (FQ susceptible)", "BPaLM (Preferred)", "Bedaquiline + Pretomanid + Linezolid + Moxifloxacin"),
                    Triple("MDR/RR-TB (FQ resistant)", "BPaL", "Bedaquiline + Pretomanid + Linezolid"),
                    Triple("Pre-XDR / complex resistance", "Modified all-oral 9-month", "Bedaquiline + Delamanid + Linezolid + Levofloxacin / Clofazimine + others"),
                    Triple("Extensive resistance / intolerance", "Individualized longer regimen", "≥5 effective agents (tailored)")
                ).forEachIndexed { index, (type, regimen, drugs) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (index % 2 == 0) Color.Transparent else MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = type, modifier = Modifier.weight(1.2f), style = MaterialTheme.typography.bodyMedium)
                        Text(text = regimen, modifier = Modifier.weight(1.3f), style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
                        Text(text = drugs, modifier = Modifier.weight(2f), style = MaterialTheme.typography.bodyMedium)
                        Text(text = if (regimen.contains("BPaLM")) "~6 months" else if (regimen.contains("9-month")) "~9 months" else ">12–18 months", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                    }
                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), thickness = 0.5.dp)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Note with subtle background
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Note: BPaLM/BPaL remains the prioritized regimen (WHO 2024–2025). Always confirm latest national guidelines and perform DST. Consult WHO consolidated guidelines (Module 4) for full details.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// Reusable Helpers (add these to your file)
// ──────────────────────────────────────────────────────────────────────────────

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 12.dp)
    )
}

@Composable
private fun ExpandableSection(
    title: String,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(true) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(
                    if (expanded)
                        Res.drawable.ExpandLess
                    else
                        Res.drawable.arrow_drop_down
                ),
                contentDescription = "Toggle section",
                tint = MaterialTheme.colorScheme.primary,            modifier = Modifier.size(24.dp) // actual icon size

            )
        }

        if (expanded) {
            content()
        }
    }
}

@Composable
private fun NumberedListItem(number: Int, text: String) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "$number.",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(end = 12.dp)
        )
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun BulletPoint(text: String) {
    Row(
        modifier = Modifier
            .padding(start = 32.dp, top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text("• ", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text(text = text)
    }
}

@Composable
private fun RowScope.TableHeaderText(text: String, weight: Float = 1f) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.weight(weight), fontSize = 10.sp,

    )
}

//Q5


@Preview(showBackground = true)
@Composable
fun PreviewPatientTypeSelectionScreen() {
    TbTreatmentScreen(


    )
}


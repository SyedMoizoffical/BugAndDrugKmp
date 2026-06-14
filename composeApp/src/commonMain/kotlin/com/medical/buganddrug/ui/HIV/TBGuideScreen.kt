package com.medical.buganddrug.ui.TB

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.medical.buganddrug.ui.HIV.GuideContent
import com.medical.buganddrug.ui.HIV.QuestionItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import buganddrug_multiplateform.composeapp.generated.resources.Res
import buganddrug_multiplateform.composeapp.generated.resources.arrow_drop_down
import com.medical.buganddrug.ui.HIV.HivCenterScreen.CenterScreen
import com.medical.buganddrug.ui.HIV.HivCenterScreen.HivArtCenterViewModel
import com.medical.buganddrug.ui.HIV.TBQuestion.AdjunctiveSteroidUseScreen
import com.medical.buganddrug.ui.HIV.TBQuestion.AntiTuberculousSideEffectsScreen
import com.medical.buganddrug.ui.HIV.TBQuestion.TbPreventiveTherapyScreen
import com.medical.buganddrug.ui.HIV.TBQuestion.TbResistanceTypesScreen
import com.medical.buganddrug.ui.HIV.TBQuestion.TbTreatmentScreen
import com.medical.buganddrug.ui.theme.cardDarkBackgroundColor
import com.medical.buganddrug.ui.theme.cardLightBackgroundColor
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject


val tbQuestions: List<QuestionItem> = listOf(
    QuestionItem(1,
        title = "1. Whom to Screen for Tuberculosis (TB)",
        details = """
Screening is recommended for:

1. Individuals with suggestive symptoms:
  - Persistent cough > 2 weeks
  - Weight loss
  - Fever
  - Night sweats

2. High-risk populations:
  - HIV-positive individuals
  - Healthcare workers
  - Prisoners
  - Diabetics
  - Immunocompromised patients

• Household contacts of confirmed TB cases
        """.trimIndent()
    ),
    QuestionItem(2,
        title = "2. How to Diagnose TB",
        details = ""
    ),
    QuestionItem(3,
        title = "3. Types of TB based on Resistance Pattern",
        details = ""
    ),
    QuestionItem(4,
        title = "4. TB Treatment",
        details = "".trimIndent()
    ),
    QuestionItem(5,
        title = "5. Adjunctive Steroid Use in TB",
        details = "".trimIndent()
    ),
    QuestionItem(6,
        title = "6. Anti-tuberculous Drugs Side Effects",
        details = """
| Drug name       | Common / Important Side Effects                                                                 |
|-----------------|-------------------------------------------------------------------------------------------------|
| Isoniazid (H)   | Nausea, fatigue, hepatotoxicity, peripheral neuropathy (give pyridoxine)                        |
| Rifampicin (R)  | Orange discoloration of urine, GI upset, hepatotoxicity, drug interactions                     |
| Pyrazinamide (Z)| Nausea, arthralgia, hepatotoxicity, hyperuricemia                                              |
| Ethambutol (E)  | GI upset, optic neuritis (monitor vision)                                                       |
| Streptomycin    | Injection site pain, ototoxicity, nephrotoxicity                                                |
| Levofloxacin    | GI upset, dizziness, QT prolongation, tendinopathy                                              |
| Moxifloxacin    | Nausea, QT prolongation                                                                         |
| Linezolid       | Nausea, headache, myelosuppression, optic neuropathy                                            |
| Bedaquiline     | Nausea, arthralgia, QT prolongation, hepatotoxicity                                             |
        """.trimIndent()
    ),
    QuestionItem(7,
        title = "7. TB Preventive Therapy (TPT)",
        details = """
Preventive therapy is essential in high-risk groups (household contacts, HIV-positive, immunosuppressed patients).
Active TB must be ruled out before starting TPT.

| Regimen | Drugs                      | Duration     | Typical Population                     |
|---------|----------------------------|--------------|----------------------------------------|
| 3HP     | Isoniazid + Rifapentine    | 3 months weekly | Adults and children ≥2 years           |
| 6H      | Isoniazid (daily)          | 6 months     | Adults and children                    |
| 3HR     | Isoniazid + Rifampicin (daily) | 3 months | Children & adolescents <15 years       |
| 4R      | Rifampicin (daily)         | 4 months     | Children & adolescents <15 years       |
| 1HP     | Isoniazid + Rifapentine (daily) | 1 month | Adults and children ≥2 years           |
        """.trimIndent()
    ),
    QuestionItem(
        8,
        title = "8. DR-TB Treatment Centres in Pakistan (Where to refer)",
        details = ""
    )
)


@Composable
fun TBGuideScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var expandedItem by remember { mutableStateOf<QuestionItem?>(null) }
    var selectedItem by remember { mutableStateOf(0) }  // renamed for clarity (camelCase)

    val filtered = tbQuestions.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.details.contains(searchQuery, ignoreCase = true)
    }

    // Main content switching logic
    when (selectedItem) {
        7 -> {


            TbPreventiveTherapyScreen(onBackClick =
                    {
                        selectedItem = 0
                        expandedItem =null
                    })

        } 6 -> {


            AntiTuberculousSideEffectsScreen(onBackClick =
                    {
                        selectedItem = 0
                        expandedItem =null
                    })

        }  5 -> {


            AdjunctiveSteroidUseScreen(onBackClick =
                    {
                        selectedItem = 0
                        expandedItem =null
                    })

        }     4 -> {


            TbTreatmentScreen(onBackClick =
                    {
                        selectedItem = 0
                        expandedItem =null
                    })

        }
        3 -> {


            TbResistanceTypesScreen(onBackClick =
                    {
                        selectedItem = 0
                        expandedItem =null
                    })

        }
        2 -> {


                TuberculosisDiagnosisScreen(onBackClick =
                    {
                        selectedItem = 0
                        expandedItem =null
                    })

        }
        8->{
            val userViewModel: HivArtCenterViewModel = koinInject()

            CenterScreen(userViewModel,onBackClick = {
                selectedItem = 0
                expandedItem =null

            },type = "TB")
        }

        else -> {
            GuideContent(
                questions = filtered,
                searchQuery = searchQuery,
                onSearchChange = { searchQuery = it },
                expandedItem = expandedItem,
                onToggle = { item ->
                    selectedItem = item.id
                    expandedItem = if (expandedItem == item) null else item
                }
            )
        }
    }
}

@Composable
fun TuberculosisDiagnosisScreen(
    onBackClick: () -> Unit = {}
) {
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
                    text = "2: How to Diagnose TB",
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                Text(
                    text = "How to Diagnose TB (2024 National TB Guidelines)",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "The 2024 National TB guidelines emphasize molecular testing as the primary diagnostic tool.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                ModernCard {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Key Diagnostic Tools",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        BulletPointItem("GeneXpert MTB/RIF: First-line rapid molecular test detects Rifampicin resistance")
                        BulletPointItem("Line Probe Assay (LPA): For drug resistance testing (rifampicin, isoniazid, fluoroquinolones, and injectables)")
                        BulletPointItem("Culture and Drug Sensitivity Testing (DST)")
                        BulletPointItem("Microscopy: AFB smear")
                        BulletPointItem("Chest X-ray")
                        BulletPointItem("Histopathology")
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "TB Diagnostic Tests Based on Site of Infection",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                ModernCard {
                    Column() {
                        // Header Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF800080))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Site of TB",
                                modifier = Modifier.weight(1f),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFFFFF),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Diagnostic Tests",
                                modifier = Modifier.weight(2f),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFFFFF),
                                textAlign = TextAlign.Center
                            )
                        }

                        Divider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)

                        TableRow(
                            site = "Pulmonary TB",
                            tests = "Sputum GeneXpert MTB/RIF or Ultra; Sputum smear microscopy, Chest X-ray; Culture & DST (if needed)"
                        )

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                        TableRow(
                            site = "Lymph Node TB",
                            tests = "FNAC with GeneXpert / AFB smear; Lymph node biopsy for histopathology, GeneXpert & AFB Culture"
                        )

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                        TableRow(
                            site = "Pleural TB",
                            tests = "Pleural fluid GeneXpert, AFB smear, ADA;  Chest X-ray; Pleural biopsy for histopathology, GeneXpert, AFB smear and culture"
                        )

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                        TableRow(
                            site = "TB Meningitis",
                            tests = "CSF GeneXpert MTB/RIF or Ultra, CSF analysis; MRI brain"
                        )

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                        TableRow(
                            site = "CNS TB (tuberculoma)",
                            tests = "MRI brain; GeneXpert on tissue if available; CT scan with contrast"
                        )

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                        TableRow(
                            site = "Bone & Joint TB",
                            tests = "X-ray; MRI affected site; GeneXpert on aspirate/biopsy, Histopathology"
                        )

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                        TableRow(
                            site = "Spinal TB (Pott disease)",
                            tests = "MRI spine; GeneXpert on tissue with histopathology, AFB smear & culture"
                        )

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                        TableRow(
                            site = "Abdominal TB",
                            tests = "Ultrasound / CT abdomen; GeneXpert on ascitic fluid, Ascitic fluid ADA; Biopsy for histopathology & culture (C/S)"
                        )

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                        TableRow(
                            site = "Genitourinary TB",
                            tests = "Urine GeneXpert MTB/RIF"
                        )

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                        TableRow(
                            site = "Pericardial TB",
                            tests = "Pericardial fluid ADA; GeneXpert, Echocardiography, AFB C/S"
                        )

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                        TableRow(
                            site = "Miliary TB",
                            tests = "Chest X-ray; Sputum GeneXpert MTB/RIF or Ultra; Sputum smear microscopy, Culture & DST (if needed), Blood culture; CT chest (if needed)"
                        )

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                        TableRow(
                            site = "Cutaneous TB",
                            tests = "Skin biopsy for histopathology, AFB smear and C/S, GeneXpert"
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Difference between GeneXpert MTB/RIF and GeneXpert Ultra",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                ModernCard {
                    Column() {
                        // Header Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF800080))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Feature",
                                modifier = Modifier.weight(1f),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFFFFF),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "GeneXpert MTB/RIF",
                                modifier = Modifier.weight(1f),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFFFFF),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "GeneXpert MTB/RIF Ultra",
                                modifier = Modifier.weight(1f),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFFFFF),
                                textAlign = TextAlign.Center
                            )
                        }

                        Divider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)

                        TableRow3(
                            feature = "Purpose",
                            rif = "Routine diagnosis of TB and rifampicin resistance",
                            ultra = "Increased detection in paucibacillary TB"
                        )

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                        TableRow3(
                            feature = "Molecular targets",
                            rif = "rpoB gene",
                            ultra = "rpoB + IS6110, IS1081"
                        )

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                        TableRow3(
                            feature = "Limit of detection",
                            rif = "~131 CFU/ml",
                            ultra = "~16 CFU/ml"
                        )

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                        TableRow3(
                            feature = "Sensitivity",
                            rif = "Good in smear-positive TB",
                            ultra = "Very high in smear-negative TB"
                        )

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                        TableRow3(
                            feature = "Specificity",
                            rif = "Very high",
                            ultra = "Slightly lower (detects dead bacilli)"
                        )

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                        TableRow3(
                            feature = "Pediatric TB",
                            rif = "Less sensitive",
                            ultra = "Preferred test"
                        )

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                        TableRow3(
                            feature = "HIV-associated TB",
                            rif = "Acceptable",
                            ultra = "Preferred test"
                        )

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                        TableRow3(
                            feature = "Extrapulmonary TB",
                            rif = "Lower yield",
                            ultra = "Higher diagnostic yield"
                        )

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                        TableRow3(
                            feature = "Trace result",
                            rif = "Not available",
                            ultra = "Available"
                        )

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                        TableRow3(
                            feature = "Rifampicin resistance detection in trace result",
                            rif = "Not applicable",
                            ultra = "Cannot be determined"
                        )

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                        TableRow3(
                            feature = "Previously treated TB",
                            rif = "Preferred",
                            ultra = "Trace needs correlation as can also detect dead bacilli"
                        )

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                        TableRow3(
                            feature = "Pakistan NTP use",
                            rif = "Routine adult pulmonary TB",
                            ultra = "Children, PLHIV, EPTB, smear-negative TB"
                        )

                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                        TableRow3(
                            feature = "Key teaching point",
                            rif = "Best for specificity",
                            ultra = "Best for sensitivity"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ModernCard(
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { /* Optional: add onToggle or remove clickable if not needed */ },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        content()
    }
}

@Composable
fun TableRow(
    site: String,
    tests: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = site,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = tests,
            modifier = Modifier
                .weight(2f)
                .padding(horizontal = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
@Composable
fun TableRow3(feature: String, rif: String, ultra: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = feature,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Medium
        )
        Text(
            text = rif,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = ultra,
            modifier = Modifier.weight(1f)
        )
    }
}
@Composable
fun BulletPointItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("•", style = MaterialTheme.typography.bodyMedium)
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}


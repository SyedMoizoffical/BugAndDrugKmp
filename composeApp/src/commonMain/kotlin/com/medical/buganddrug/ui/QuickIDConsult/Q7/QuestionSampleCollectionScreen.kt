package com.medical.buganddrug.ui.QuickIDConsult.Q7

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import buganddrug_multiplateform.composeapp.generated.resources.Res
import buganddrug_multiplateform.composeapp.generated.resources.first_aid_kit
import buganddrug_multiplateform.composeapp.generated.resources.info
import com.medical.buganddrug.ui.QuickIDConsult.Q3.SingleSelectSearchableSpinnerDialog
import com.medical.buganddrug.ui.QuickIDConsult.topBar
import com.medical.buganddrug.ui.theme.cardLightBackgroundColor
import com.medical.buganddrug.ui.theme.cardSoftBackgroundColor
import org.jetbrains.compose.resources.painterResource

@Composable
fun QuestionSampleCollectionScreen(
    onBackClick: () -> Unit = {},
) {
    val options = listOf(
        "Urine Culture (Non-catheterized patient)",
        "Urine Culture (Catheterized patient)",
        "Blood Culture"
    )

    var selectedOption by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            topBar(
                topic = "Collect Urine & Blood Cultures",
                patientType = "Sample Collection",
                onBackClick = onBackClick
            )
        },
        containerColor = Color.Transparent
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
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Dropdown for selecting culture type
                SingleSelectSearchableSpinnerDialog(
                    label = "Select Sample Type",
                    items = options.map { it to it },
                    itemLabel = { it.first },
                    selectedItem = selectedOption,
                    onItemSelected = { selectedOption = it?.second }
                )

                // Show placeholder if nothing is selected
                if (selectedOption == null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.first_aid_kit),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Please select a sample type from above to view collection instructions.",
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                fontSize = 16.sp,
                                lineHeight = 22.sp
                            )
                        }
                    }
                } else {
                    // ✅ Main Content when item is selected
                    SelectedSampleCard(selectedOption!!)
                }
            }
        }
    }
}

@Composable
private fun SelectedSampleCard(selected: String) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = selected,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Divider(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 40.dp)
            )

            when (selected) {
                "Urine Culture (Non-catheterized patient)" -> {
                    SectionCard("Equipment Required") {
                        BulletText("Sterile container")
                    }
                    SectionCard("Steps (Instructions for Patients)") {
                        BulletList(
                            listOf(
                                "Wash hands with soap and water",
                                "Wash perineal area with soap and water",
                                "Females: Part the labia with fingers",
                                "Pass a steady stream of urine and collect midstream urine",
                                "Let the first part of urine fall into toilet, then collect rest in sterile container",
                                "Close container with the lid",
                                "Place it on the sample collection rack and inform hospital staff",
                                "Wash hands again",
                                "Send sample immediately to lab (refrigerate if delay > 2h)"
                            )
                        )
                    }
                    SectionCard("Important Precautions", highlight = true) {
                        BulletList(
                            listOf(
                                "Do not collect first few mL of urine (increases contamination)",
                                "Avoid touching inside of container or lid",
                                "Do not leave sample >2 h at room temperature"
                            )
                        )
                    }
                }

                "Urine Culture (Catheterized patient)" -> {
                    SectionCard("Equipment Required") {
                        NumberList(
                            listOf(
                                "Sterile gloves",
                                "Alcohol swabs or antiseptic solution (e.g., 70% isopropyl alcohol or chlorhexidine)",
                                "Sterile syringe (10–20 mL) and needle or adaptor",
                                "Sterile urine culture container (labeled)",
                                "Disposable towel or drape",
                                "Hand hygiene supplies"
                            )
                        )
                    }

                    SectionCard("Steps") {
                        BulletList(
                            listOf(
                                "Verify order and identify patient",
                                "Confirm indication for urine culture",
                                "Perform hand hygiene before and after procedure",
                                "Clamp catheter tubing for 15–30 minutes",
                                "Disinfect sampling port and let dry",
                                "Aspirate 10–20 mL of urine using sterile syringe",
                                "Transfer into sterile container",
                                "Unclamp catheter and label specimen",
                                "Send to lab within 2 hours (refrigerate if delayed)"
                            )
                        )
                    }

                    SectionCard("Important Precautions", highlight = true) {
                        BulletList(
                            listOf(
                                "Never collect urine from drainage bag",
                                "Maintain aseptic technique",
                                "If catheter >2 weeks old, replace before sampling"
                            )
                        )
                    }
                }

                "Blood Culture" -> {
                    SectionCard("Equipment Required") {
                        BulletList(
                            listOf(
                                "Blood culture bottles",
                                "Alcohol swabs",
                                "Tourniquet",
                                "Sterile gloves",
                                "Povidone iodine or chlorhexidine",
                                "Syringe/butterfly needle",
                                "Band aid or tape",
                                "Sterile cotton/gauze"
                            )
                        )
                    }

                    SectionCard("Steps") {
                        BulletList(
                            listOf(
                                "Label bottles properly",
                                "Disinfect rubber tops",
                                "Confirm patient identity",
                                "Perform hand hygiene and wear gloves",
                                "Disinfect venipuncture site",
                                "Collect required blood volume aseptically",
                                "Adults: 20–30 mL per set, Children: 1–5 mL",
                                "Send to lab immediately (do not refrigerate)"
                            )
                        )
                    }

                    SectionCard("Important Points", highlight = true) {
                        BulletList(
                            listOf(
                                "Collect 2–3 sets from separate sites",
                                "Avoid drawing from catheters unless indicated",
                                "Collect before antibiotics when possible"
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    highlight: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .background(
                Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        content()
    }
}

@Composable
private fun BulletText(text: String) {
    Text(
        text = "• $text",
        lineHeight = 22.sp,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun BulletList(items: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items.forEach { BulletText(it) }
    }
}

@Composable
private fun NumberList(items: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items.forEachIndexed { i, t ->
            Text(
                text = "${i + 1}. $t",
                lineHeight = 22.sp,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

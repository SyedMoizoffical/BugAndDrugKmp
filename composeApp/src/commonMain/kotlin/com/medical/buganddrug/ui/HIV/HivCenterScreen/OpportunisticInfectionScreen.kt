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
import buganddrug_multiplateform.composeapp.generated.resources.ExpandLess
import buganddrug_multiplateform.composeapp.generated.resources.Res
import buganddrug_multiplateform.composeapp.generated.resources.arrow_drop_down
import org.jetbrains.compose.resources.painterResource

data class OpportunisticInfectionEntry(

    val symptomCluster: String,
    val syndrome: String,
    val possibleInfections: String
)

@Composable
fun OpportunisticInfectionScreen(onBackClick: () -> Unit = {}) {

    val entries = remember {
        listOf(
            OpportunisticInfectionEntry(
                symptomCluster = "Generalized lymphadenopathy + Fever\n\nFever (persistent)\n• Weight loss\n• Night sweats\n• Fatigue\n• Lymphadenopathy\n• Hepatosplenomegaly\n• Pyrexia of Unknown Origin (PUO)",
                syndrome = "Constitutional symptoms",
                possibleInfections =
                "Disseminated TB\n" +
                "• Disseminated MAC\n" +
                "• Histoplasmosis (disseminated)\n" +
                "• Coccidioidomycosis\n" +
                "• CMV systemic infection\n" +
                "• Bartonella (bacillary angiomatosis)\n" +
                "• Non-Hodgkin lymphoma\n" +
                "• Kaposi sarcoma"
            ),
            OpportunisticInfectionEntry(
                symptomCluster =
                "Cough\n" +
                "• Fever\n" +
                "• Dyspnea\n" +
                "• Hypoxia\n" +
                "• Chest pain",
                syndrome = "Pneumonia / Lower Respiratory Infection",
                possibleInfections =
                "Pneumocystis jirovecii pneumonia (PJP)\n" +
                "• Pulmonary TB\n" +
                "• Bacterial pneumonia (Strep, H. influenzae)\n" +
                "• Histoplasmosis\n" +
                "• Cryptococcal pneumonia\n" +
                "• CMV pneumonitis\n" +
                "• Nocardiosis\n" +
                "• Aspergillosis"
            ),
            OpportunisticInfectionEntry(
                symptomCluster =
                "Headache\n" +
                "• Fever\n" +
                "• Neck stiffness\n" +
                "• Confusion\n" +
                "• Seizures\n" +
                "• Focal neurological deficits",
                syndrome =
                "Meningitis\n" +
                "• Encephalitis\n" +
                "• Space-occupying brain lesion",
                possibleInfections =
                "Cryptococcal meningitis\n" +
                "• TB meningitis\n" +
                "• Toxoplasmosis encephalitis\n" +
                "• Progressive multifocal leukoencephalopathy (PML)\n" +
                "• CNS lymphoma (AIDS defining malignancy)\n" +
                "• CMV encephalitis\n" +
                "• Neurosyphilis"
            ),
            OpportunisticInfectionEntry(
                symptomCluster =
                "Diarrhea (acute or chronic)\n" +
                "• Abdominal pain\n" +
                "• Nausea/vomiting\n" +
                "• Weight loss",
                syndrome = "Chronic Diarrhea Syndrome",
                possibleInfections =
                "Chronic Diarrhea / Enteritis:\n" +
                "• Cryptosporidiosis\n" +
                "• Isosporiasis\n" +
                "• Microsporidiosis\n" +
                "• CMV colitis\n" +
                "• MAC enteritis\n" +
                "• Bacterial infections (Salmonella/Shigella/Campylobacter)\n" +
                "• Giardiasis"
            ),
            OpportunisticInfectionEntry(
                symptomCluster = "Painful swallowing (Odynophagia)",
                syndrome = "Esophagitis",
                possibleInfections =
                "Candida esophagitis, CMV esophagitis, HSV esophagitis"
            ),
            OpportunisticInfectionEntry(
                symptomCluster = "Oral white patches",
                syndrome = "Oral cavity infection",
                possibleInfections =
                "Oral candidiasis (thrush), Oral hairy leukoplakia (EBV)"
            ),
            OpportunisticInfectionEntry(
                symptomCluster =
                "Genital ulcers\n" +
                "• Vaginal discharge\n" +
                "• Dysuria\n" +
                "• Pelvic pain",
                syndrome =
                "Genital ulcer disease\n" +
                "• Vaginitis\n" +
                "• PID",
                possibleInfections =
                "HSV recurrent genital ulcers\n" +
                "• HPV warts\n" +
                "• Recurrent candidal vulvovaginitis\n" +
                "• Chronic pelvic inflammatory disease (PID)"
            ),
            OpportunisticInfectionEntry(
                symptomCluster =
                "Vision loss\n" +
                "• Floaters\n" +
                "• Eye pain\n" +
                "• Photophobia",
                syndrome = "Retinitis / Ocular Infection",
                possibleInfections =
                "CMV retinitis\n" +
                "• Toxoplasmosis chorioretinitis\n" +
                "• HSV keratitis\n" +
                "• Herpes zoster ophthalmicus"
            ),
            OpportunisticInfectionEntry(
                symptomCluster =
                "Rash\n" +
                "• Nodules\n" +
                "• Non-healing ulcers\n" +
                "• Vesicles\n" +
                "• Warts",
                syndrome =
                "Dermatomucosal lesions\n" +
                "• Ulcerative lesions",
                possibleInfections =
                "Kaposi sarcoma\n" +
                "• Herpes zoster\n" +
                "• HSV chronic ulcers\n" +
                "• Molluscum contagiosum\n" +
                "• Bacillary angiomatosis\n" +
                "• Fungal skin infections\n" +
                "• HPV lesions"
            )
        )
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Opportunistic Infections by Symptom Cluster",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onBackClick) {
                    Icon(painter = painterResource(Res.drawable.arrow_drop_down), contentDescription = "Back")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(entries) { entry ->
                OpportunisticInfectionCard(entry)
            }
        }
    }
}
@Composable
private fun OpportunisticInfectionCard(entry: OpportunisticInfectionEntry) {

    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)    ) {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = entry.symptomCluster,
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
                    contentDescription = null,
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Divider()
                    Spacer(modifier = Modifier.height(12.dp))

                    DetailRow("Syndrome", entry.syndrome)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailRow("Possible Opportunistic Infections", entry.possibleInfections)
                }
            }
        }
    }
}

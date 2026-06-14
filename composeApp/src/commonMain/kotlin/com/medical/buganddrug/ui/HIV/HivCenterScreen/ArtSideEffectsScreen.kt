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
import kotlinx.serialization.Serializable

@Serializable
data class ArtSideEffectEntry(
    val drug: String,
    val artClass: String,
    val sideEffects: String
)


@Composable
fun ArtSideEffectsScreen(onBackClick: () -> Unit = {}) {

    val entries = remember {
        listOf(
            ArtSideEffectEntry(
                "Tenofovir disoproxil fumarate (TDF)",
                "NRTI",
                "Renal dysfunction, Fanconi syndrome, hypophosphatemia, reduced bone mineral density, flatulence"
            ),
            ArtSideEffectEntry(
                "Tenofovir alafenamide (TAF)",
                "NRTI",
                "Weight gain, lipid increase (low renal and bone toxicity)"
            ),
            ArtSideEffectEntry(
                "Lamivudine (3TC)",
                "NRTI",
                "Very well tolerated; rare lactic acidosis, pancreatitis"
            ),
            ArtSideEffectEntry(
                "Emtricitabine (FTC)",
                "NRTI",
                "Skin hyperpigmentation, mild GI upset"
            ),
            ArtSideEffectEntry(
                "Abacavir (ABC)",
                "NRTI",
                "Hypersensitivity reaction, possible ↑ cardiovascular risk"
            ),
            ArtSideEffectEntry(
                "Zidovudine (AZT)",
                "NRTI",
                "Anemia, neutropenia, nausea, abdominal discomfort, headache, insomnia, myopathy"
            ),
            ArtSideEffectEntry(
                "Efavirenz (EFV)",
                "NNRTI",
                "Vivid dreams, insomnia, hallucination, hepatotoxicity"
            ),
            ArtSideEffectEntry(
                "Nevirapine (NVP)",
                "NNRTI",
                "Severe rash (SJS), hepatotoxicity, hypersensitivity"
            ),
            ArtSideEffectEntry(
                "Dolutegravir (DTG)",
                "INSTI",
                "Weight gain, insomnia, headache"
            ),
            ArtSideEffectEntry(
                "Raltegravir (RAL)",
                "INSTI",
                "Myopathy, Creatinine kinase elevation"
            ),
            ArtSideEffectEntry(
                "Bictegravir (BIC)",
                "INSTI",
                "Weight gain, nausea, benign creatinine rise"
            ),
            ArtSideEffectEntry(
                "Lopinavir/ritonavir (LPV/r)",
                "PI",
                "GI upset, hyperlipidemia, insulin resistance"
            ),
            ArtSideEffectEntry(
                "Atazanavir (ATV)",
                "PI",
                "Indirect hyperbilirubinemia(cosmetic only), nephrolithiasis"
            ),
            ArtSideEffectEntry(
                "Darunavir (DRV/r or DRV/c)",
                "PI",
                "Rash, hepatotoxicity, GI upset"
            ),
            ArtSideEffectEntry(
                "Enfuvirtide (ENF)",
                "Fusion inhibitor",
                "Painful injection site reactions, increase pneumonia risk"
            ),
            ArtSideEffectEntry(
                "Maraviroc (MVC)",
                "CCR5 inhibitor",
                "Hepatotoxicity, hypotension, rash"
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
                    text = "10. ART Side Effects",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(Res.drawable.arrow_drop_down),                        contentDescription = "Back",
                        modifier = Modifier.rotate(180f).size(24.dp),

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
                ArtSideEffectCard(entry)
            }
        }
    }
}
@Composable
private fun ArtSideEffectCard(entry: ArtSideEffectEntry) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)    ) {
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
                    Text(
                        text = entry.drug,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = entry.artClass,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Icon(
                    painter = painterResource(
                        if (expanded)
                            Res.drawable.ExpandLess
                        else
                            Res.drawable.arrow_drop_down
                    ),
                    contentDescription = null,            modifier = Modifier.size(24.dp) // actual icon size
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = entry.sideEffects,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

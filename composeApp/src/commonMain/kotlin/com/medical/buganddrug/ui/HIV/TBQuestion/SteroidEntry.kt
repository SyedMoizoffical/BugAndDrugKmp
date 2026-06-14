package com.medical.buganddrug.ui.HIV.TBQuestion

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

data class SteroidEntry(
    val condition: String,
    val drug: String,
    val dose: String,
    val duration: String
)

@Composable
fun AdjunctiveSteroidUseScreen(onBackClick: () -> Unit = {}) {
    val entries = remember {
        listOf(
            SteroidEntry(
                "TB meningitis (adults)",
                "Dexamethasone",
                "0.4 mg/kg/day IV initially",
                "0.4 mg/kg/day week 1,\n0.3 mg/kg/day week 2,\n0.2 mg/kg/day week 3,\n0.1 mg/kg/day week 4,\nthen tapered to stop over 3-4 weeks"
            ),
            SteroidEntry(
                "Pericardial TB (adults) in selective cases only",
                "Prednisone",
                "60 mg/day orally for 1–2 weeks",
                "Taper gradually over 6–8 weeks to 5–10 mg/day"
            ),
            SteroidEntry(
                "TB-IRIS (HIV)",
                "Prednisone",
                "1–2 mg/kg/day orally",
                "Taper over 4–6 weeks based on response"
            ),
            SteroidEntry(
                "Severe miliary TB / ARDS",
                "Prednisone / Methylprednisolone",
                "1 mg/kg/day orally or IV equivalent",
                "Usually 2–4 weeks with taper based on clinical response"
            ),
            SteroidEntry(
                "Airway obstruction / compressive lymphadenitis",
                "Prednisone",
                "1–2 mg/kg/day",
                "2–4 weeks with taper based on symptom resolution"
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
                // Title first, takes available space
                Text(
                    text = "5. Adjunctive Steroid Use in TB",
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
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(entries) { entry ->
                SteroidCard(entry)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Doses are for adults unless specified. Always follow local guidelines and consult an infectious disease specialist.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun SteroidCard(entry: SteroidEntry) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(16.dp)
        ) {
            // Header row (always visible)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = entry.condition,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = entry.drug,
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
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp) // actual icon size


                )
            }

            // Details (expandable)
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(
                    animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
                ) + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    Divider(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        thickness = 1.dp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    DetailRow("Dose", entry.dose)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailRow("Duration / Taper", entry.duration)
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(2f)
        )
    }
}
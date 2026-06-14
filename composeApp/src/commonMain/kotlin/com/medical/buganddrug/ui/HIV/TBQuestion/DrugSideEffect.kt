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
import androidx.compose.ui.unit.sp
import buganddrug_multiplateform.composeapp.generated.resources.ExpandLess
import buganddrug_multiplateform.composeapp.generated.resources.Res
import buganddrug_multiplateform.composeapp.generated.resources.arrow_drop_down
import org.jetbrains.compose.resources.painterResource

data class DrugSideEffect(
    val drug: String,
    val sideEffects: String
)

@Composable
fun AntiTuberculousSideEffectsScreen(onBackClick: () -> Unit = {}) {
    val entries = remember {
        listOf(
            DrugSideEffect(
                "Isoniazid (H)",
                "Nausea, fatigue, hepatotoxicity, peripheral neuropathy (give pyridoxine)"
            ),
            DrugSideEffect(
                "Rifampicin (R)",
                "Orange discoloration of urine, GI upset, hepatotoxicity, drug interactions"
            ),
            DrugSideEffect(
                "Pyrazinamide (Z)",
                "Nausea, arthralgia, hepatotoxicity, hyperuricemia"
            ),
            DrugSideEffect(
                "Ethambutol (E)",
                "GI upset, optic neuritis (monitor vision)"
            ),
            DrugSideEffect(
                "Streptomycin",
                "Injection site pain, ototoxicity, nephrotoxicity"
            ),
            DrugSideEffect(
                "Levofloxacin",
                "GI upset, dizziness, QT prolongation, tendinopathy"
            ),
            DrugSideEffect(
                "Moxifloxacin",
                "Nausea, QT prolongation"
            ),
            DrugSideEffect(
                "Linezolid",
                "Nausea, headache, myelosuppression, optic neuropathy"
            ),
            DrugSideEffect(
                "Bedaquiline",
                "Nausea, arthralgia, QT prolongation, hepatotoxicity"
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
                    text = "6. Anti-tuberculous Drugs Side Effects",
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
                DrugSideEffectCard(entry)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Always monitor patients closely and follow local guidelines. Hepatotoxicity and QT prolongation require special attention.",
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
private fun DrugSideEffectCard(entry: DrugSideEffect) {
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
            // Header (always visible)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = entry.drug,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )


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

            // Side effects (expandable)
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

                    Text(
                        text = entry.sideEffects,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}
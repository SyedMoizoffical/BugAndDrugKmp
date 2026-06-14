package com.medical.buganddrug.ui.HIV.TBQuestion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import buganddrug_multiplateform.composeapp.generated.resources.Res
import buganddrug_multiplateform.composeapp.generated.resources.arrow_drop_down
import org.jetbrains.compose.resources.painterResource

@Composable
fun TbPreventiveTherapyScreen(onBackClick: () -> Unit = {}) {
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
                    text = "7. TB Preventive Therapy (TPT)",
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
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Main Title
            item {
                Text(
                    text = "TB Preventive Therapy",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Introduction
            item {
                Text(
                    text = "Preventive therapy (TPT) is essential to stop TB progression in high-risk groups.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Eligibility Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Who Should Receive TPT?",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "• Household contacts of TB patients\n" +
                                    "• People living with HIV (regardless of CD4 count)\n" +
                                    "• Immunosuppressed individuals (e.g., organ transplant, TNF inhibitors)",
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 24.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Warning
            item {
                Text(
                    text = "Active TB must be ruled out before starting preventive therapy.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Table Section
            stickyHeader {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val headers = listOf("Regimen", "Drugs", "Duration", "Typical Population")
                        val weights = listOf(0.9f, 1.3f, 1.1f, 1.6f) // tuned for content length

                        headers.forEachIndexed { i, title ->
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontSize = 10.sp,           // ← slightly bigger → better readability
                                lineHeight = 18.sp,
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                                    .weight(weights[i])
                                    .padding(horizontal = 6.dp)  // ← more breathing room
                            )
                        }
                    }
                }
            }

            // ————————————————————————————————————
// Sticky Header
// ————————————————————————————————————


// ————————————————————————————————————
// Table Body Rows
// ————————————————————————————————————
            val regimens = listOf(
                Regimen("3HP", "Isoniazid + Rifapentine", "3 months (weekly)", "Adults & children ≥2 years"),
                Regimen("6H", "Isoniazid (daily)", "6 months", "All ages"),
                Regimen("3HR", "Isoniazid + Rifampicin (daily)", "3 months", "Children & adolescents <15 years"),
                Regimen("4R", "Rifampicin (daily)", "4 months", "Children & adolescents <15 years"),
                Regimen("1HP", "Isoniazid + Rifapentine (daily)", "1 month", "Adults & children ≥2 years")
            )

            itemsIndexed(regimens) { index, regimen ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp, vertical = 0.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            index % 2 == 0 -> MaterialTheme.colorScheme.surface
                            else -> MaterialTheme.colorScheme.surface
                        }
                    ),
                    elevation = CardDefaults.cardElevation(0.5.dp) // very subtle
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val weights = listOf(0.9f, 1.3f, 1.1f, 1.6f)

                        Text(
                            text = regimen.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .weight(weights[0])
                                .padding(end = 12.dp)          // ← separation between columns
                        )

                        Text(
                            text = regimen.drugs,
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 12.sp,

                            modifier = Modifier
                                .weight(weights[1])
                                .padding(end = 12.dp)
                        )

                        Text(
                            text = regimen.duration,
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 12.sp,

                            modifier = Modifier
                                .weight(weights[2])
                                .padding(end = 12.dp)
                        )

                        Text(
                            text = regimen.population,
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 12.sp,

                            modifier = Modifier.weight(weights[3])
                        )
                    }
                }
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

data class Regimen(
    val name: String,
    val drugs: String,
    val duration: String,
    val population: String
)

@Preview(showBackground = true)
@Composable
fun TbPreventiveTherapyPreview() {
    MaterialTheme {
        TbPreventiveTherapyScreen()
    }
}
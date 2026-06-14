package com.medical.buganddrug.ui.HIV

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import buganddrug_multiplateform.composeapp.generated.resources.Res
import buganddrug_multiplateform.composeapp.generated.resources.arrow_drop_down
import buganddrug_multiplateform.composeapp.generated.resources.search
import com.medical.buganddrug.ui.HIV.HivCenterScreen.HivArtCenterViewModel
import com.medical.buganddrug.ui.QuickIDConsult.topBar
import com.medical.buganddrug.ui.TB.TBGuideScreen
import com.medical.buganddrug.ui.theme.md_theme_light_shadow
import org.jetbrains.compose.resources.painterResource
import kotlinx.serialization.Serializable

// -------------------- Data Models & Enums --------------------
@Serializable
data class QuestionItem(
    val id: Int,
    val title: String,
    val details: String
)

enum class GuideType(val title: String) {
    HIV("HIV"),
    TB("TB"),
    STI("STI")   // Changed from MB to STI for clarity
}






// -------------------- Shared Composables --------------------

@Composable
fun GuideSelector(
    selected: GuideType,
    onSelect: (GuideType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        GuideType.values().forEach { type ->
            val isSelected = selected == type

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .border(
                        width = 1.dp,
                        color = if (isSelected) Color(0xFF800080) else Color(0xFF800080),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .background(
                        color = if (isSelected) Color(0xFF800080) else Color.White,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .clickable { onSelect(type) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = type.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) Color.White else Color(0xFF800080)
                )
            }
        }
    }
}

@Composable
fun QuestionCard(
    question: QuestionItem,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onToggle() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = question.title,
                    modifier = Modifier.weight(1f),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                        color = md_theme_light_shadow
                    )                )

                Icon(
                    painter = painterResource(Res.drawable.arrow_drop_down),
                    contentDescription = null,
                    modifier = Modifier
                        .size(28.dp)
                        .rotate(if (isExpanded) 180f else 0f),
                    tint = Color(0xFF800080)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        color = Color(0xFFF0F0F0)
                    )
                    val lines = remember(question.details) { question.details.lines() }
                    val headingRegex = Regex("""^\s*\d+(\.\d+)*\.?\s""")

                    Text(
                        text = buildAnnotatedString {
                            lines.forEachIndexed { index, line ->

                                val isHeading = headingRegex.containsMatchIn(line)

                                withStyle(
                                    SpanStyle(
                                        color = if (isHeading) Color(0xFF800080) else Color(0xFF1B2B5D),
                                        fontWeight = if (isHeading) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                ) {
                                    append(line)
                                }

                                if (index < lines.lastIndex) {
                                    append("\n")
                                }
                            }
                        },
                        modifier = Modifier.padding(20.dp),
                        lineHeight = 24.sp
                    )


                }
            }
        }
    }
}







@Composable
fun GuideContent(
    questions: List<QuestionItem>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    expandedItem: QuestionItem?,
    onToggle: (QuestionItem) -> Unit
) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Search...") },
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.search),
                    contentDescription = null,
                    tint = Color(0xFF800080)
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (questions.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No data available",
                    color = Color(0xFF666666)
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 32.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(questions) { question ->
                    QuestionCard(
                        question = question,
                        isExpanded = expandedItem == question,
                        onToggle = { onToggle(question) }
                    )
                }
            }
        }
    }
}

// -------------------- Main Screen with Tabs --------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicalGuideScreen(
    viewModel: HivArtCenterViewModel,
    onBackClick: () -> Unit = {}
) {
    var selectedGuide by remember { mutableStateOf(GuideType.HIV) }

    Scaffold(
        topBar = {
            topBar(
                topic = "Clinical Guide",
                patientType = selectedGuide.title,
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
                .padding(innerPadding)
        ) {
            Column {
                Spacer(modifier = Modifier.height(8.dp))

                // Tabs
                GuideSelector(
                    selected = selectedGuide,
                    onSelect = { selectedGuide = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Content based on selected tab
                when (selectedGuide) {
                    GuideType.HIV -> HIVGuideScreen(viewModel)
                    GuideType.TB -> TBGuideScreen()
                    GuideType.STI -> STIManagementScreen()
                }
            }
        }
    }
}
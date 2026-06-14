package com.medical.buganddrug.ui.QuickIDConsult

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import buganddrug_multiplateform.composeapp.generated.resources.Res
import buganddrug_multiplateform.composeapp.generated.resources.info
import org.jetbrains.compose.resources.painterResource

@Composable
fun QuickIDConsults(
    topic: String,
    patientType: String,
    onBackClick: () -> Unit = {},
    onQuestionClick: (String) -> Unit = {}
) {
    data class Question(val id: Int, val title: String)

    val askMeQuestions = listOf(
        Question(1, "Sepsis Score calculator/predictor: When to Start Antibiotics?"),
        Question(2, "Clinical Syndrome Identification & the diagnostic workup"),
        Question(3, "IV to PO Antimicrobial Switch"),
        Question(4, "Isolation Precaution Finder"),
        Question(5, "Collect Urine & Blood Cultures"),
        Question(6, "Cultured Guided Therapy"),
        Question(7, "Antimicrobial Resistance / Resistant Genes"),
        Question(8, "Post-Exposure Prophylaxis"),
    )

    Scaffold(
        topBar = { topBar (topic = topic, patientType = patientType, onBackClick = onBackClick) },
        containerColor = Color.Transparent
    ) { paddingValues ->
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
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Select Your Question",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (askMeQuestions.isEmpty()) {
                    Text(
                        text = "No questions available for this topic yet.",
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.error)
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(askMeQuestions) { question ->
                            QuestionCard(
                                question = question.title,
                                onClick = { onQuestionClick(question.id.toString()) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionCard(
    question: String,
    onClick: () -> Unit
) {

    var selected by remember { mutableStateOf(false) }

    val backgroundColor by animateColorAsState(
        if (selected) Color(0xFF800080) else Color.White
    )
    val scale by animateFloatAsState(if (selected) 1.03f else 1f)
    val textColor = if (selected) Color.White else Color(0xFF000002)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                selected = !selected
                onClick()
            },
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Text(
            text = question,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,
                color = textColor
            ),
            modifier = Modifier.padding(20.dp)
        )
    }
}

@Composable
fun topBar(
    onBackClick: () -> Unit, topic: String, patientType: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF6A1B9A),
                            Color(0xFFCE93D8)

                        )
                    ),
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(Res.drawable.info),
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = topic,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                    Text(
                        text = patientType,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewQuickIDConsult() {
    QuickIDConsults(
        topic = "Quick ID Consult",
        patientType = "In Patients",
        onBackClick = {},
        onQuestionClick = {}
    )
}




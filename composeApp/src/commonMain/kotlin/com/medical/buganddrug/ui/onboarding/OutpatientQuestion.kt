package com.medical.buganddrug.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medical.buganddrug.ui.QuickIDConsult.topBar
import com.medical.buganddrug.ui.theme.md_theme_light_shadow

@Composable
fun OutpatientQuestion(
    onPatientTypeClick: (topic: String, patientType: String) -> Unit = { _, _ -> },
    onBackClick: () -> Unit = {}
)

{

    data class Question(val id: Int, val title: String)

    val askMeQuestions = listOf(
        Question(1, "Clinical Syndrome"),
        Question(2, "Etiological Agent"),
        Question(3, "Antimicrobials"),
        Question(4, "Creatinine Clearance Calculator"),
        Question(5, "TB / HIV / STI"),
        Question(6, "Post-Exposure Prophylaxis"),
        Question(7, "Vaccination"),


        )
    Scaffold(
        topBar = {
            topBar(
                topic = "Out Patient",
                patientType = "Select your topic",
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
                .padding(horizontal = 20.dp, vertical = 16.dp)

        )  {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                text = "Choose a Topic",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(askMeQuestions) { topic ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                onPatientTypeClick(topic.id.toString(), "Inpatient")
                            },
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Text(
                            text = topic.title,
                            modifier = Modifier.padding(20.dp),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium,
                                color = md_theme_light_shadow
                            )
                        )
                    }
                }
            }
        }}

    }

}
@Preview(showBackground = true)
@Composable
fun PreviewOutPatientScreen() {
    OutpatientQuestion(

    )
}
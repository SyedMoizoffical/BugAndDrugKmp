package com.medical.buganddrug.ui.onboarding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState

@Composable
fun DisclaimerDialog(onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x80000000)), // Semi-transparent overlay
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .fillMaxHeight(0.7f),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Scrollable content
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState)
                ) {
                    Text(
                        text = "Bug & Drug App",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF800080)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Privacy Policy, Disclaimer & Terms of Use",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Last updated: January 2026",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val points = listOf(
                        "1. Medical Disclaimer\n" +
                                "The Bug & Drug App provides provisional medical insights based on user-inputted symptoms, clinical signs, and related information. This content is generated for educational and clinical decision-support purposes only.\n" +
                                "This application:\n" +
                                "- Does not provide definite medical diagnoses\n" +
                                "- Does not prescribe medication\n" +
                                "- Does not replace professional clinical judgment\n" +
                                "Always seek the advice of a licensed and qualified healthcare professional before making any medical decisions.\n" +
                                "All final medical decisions remain the responsibility of the treating physician or licensed healthcare provider.",

                        "2. Privacy Policy\n" +
                                "2.1 Information We Collect\n" +
                                "We collect only the minimum data required to provide our services:\n" +
                                "- Clinical inputs (symptoms, signs)\n" +
                                "- Basic user details (for authentication and usage control)\n" +
                                "- Technical data (device type, OS version, error logs)\n" +
                                "2.2 How We Use Your Information\n" +
                                "Your data is used solely for:\n" +
                                "- Generating medical insights\n" +
                                "- Improving app functionality\n" +
                                "- Enhancing system security\n" +
                                "- Troubleshooting and analytics\n" +
                                "2.3 Data Protection & Security\n" +
                                "We use industry-standard security measures, including encryption, secure servers, and role-based access control. However, no system can guarantee absolute security.\n" +
                                "2.4 Data Sharing\n" +
                                "We do not sell, rent, or trade your data. Information may only be shared if required by law or regulatory authorities.\n" +
                                "2.5 Data Retention\n" +
                                "Data is retained only as long as necessary for service delivery and legal compliance.\n" +
                                "2.6 User Rights\n" +
                                "Users may request access, correction, or deletion of their data, subject to applicable laws.",

                        "3. Permissions & Device Access\n" +
                                "The app may request access to:\n" +
                                "- Internet connectivity\n" +
                                "- Device storage\n" +
                                "- Notifications",

                        "4. Terms of Use\n" +
                                "4.1 Intended Users\n" +
                                "This app is intended for licensed medical professionals only.\n" +
                                "4.2 User Responsibilities\n" +
                                "Users must provide accurate information and maintain the confidentiality of credentials.\n" +
                                "4.3 Limitation of Liability\n" +
                                "The developers are not responsible for medical decisions made solely based on app suggestions.\n" +
                                "4.4 Account Suspension\n" +
                                "Misuse may result in suspension or termination.",

                        "5. Policy Updates\n" +
                                "We may update this policy periodically.",

                        "6. Contact Information\n" +
                                "Email: Muneeba.ahsan@duhs.edu.pk\n" +
                                "Organization: Dow University of Health Sciences, Karachi."
                    )

                    // Split each point into lines and color only headings
                    points.forEach { point ->
                        point.lines().forEach { line ->
                            val trimmed = line.trimStart()
                            val isHeading = trimmed.matches(Regex("^\\d+(\\.\\d+)?\\..*"))
                            Text(
                                text = line,
                                fontSize = 14.sp,
                                fontWeight = if (isHeading) FontWeight.Bold else FontWeight.Normal,
                                color = if (isHeading) Color(0xFF800080) else Color.Black,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // Fixed button at bottom
                GradientButton(
                    text = "OK",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    onClick = { onDismiss() }
                )
            }
        }
    }
}




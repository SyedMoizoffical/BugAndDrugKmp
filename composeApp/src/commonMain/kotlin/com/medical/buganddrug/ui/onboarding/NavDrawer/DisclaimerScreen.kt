package com.medical.buganddrug.ui.onboarding.NavDrawer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.medical.buganddrug.ui.QuickIDConsult.topBar
import com.medical.buganddrug.ui.onboarding.GradientButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisclaimerScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = { topBar(onBackClick, "Disclaimer & Policy", patientType = "Please take a moment to carefully read this") }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Bug & Drug App",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color(0xFF800080)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Privacy Policy, Disclaimer & Terms of Use",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Last updated: January 2026",
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

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

                points.forEach { point ->
                    point.lines().forEach { line ->
                        val trimmed = line.trimStart()
                        val isHeading = trimmed.matches(Regex("^\\d+(\\.\\d+)?\\..*")) ||
                                trimmed.startsWith("2.") || trimmed.startsWith("4.")

                        Text(
                            text = line,
                            fontSize = 15.sp,
                            fontWeight = if (isHeading) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isHeading) Color(0xFF800080) else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

            // Bottom action button (optional - you can remove if you prefer only top bar back)


            GradientButton(
                text = "I Understand",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = {
                    onBackClick
                }
            )

        }
    }
}

// ────────────────────────────────────────────────
//              Preview
// ────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun DisclaimerScreenPreview() {
    MaterialTheme {
        DisclaimerScreen(onBackClick = {})
    }
}
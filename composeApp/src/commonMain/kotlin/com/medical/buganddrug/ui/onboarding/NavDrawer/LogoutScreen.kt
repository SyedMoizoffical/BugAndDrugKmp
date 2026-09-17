package com.medical.buganddrug.ui.onboarding.NavDrawer  // or ui.onboarding / ui.auth — your choice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.medical.buganddrug.data.remote.SharedPreferenceManager
import com.medical.buganddrug.ui.QuickIDConsult.topBar
import com.medical.buganddrug.ui.onboarding.GradientButton
import com.medical.buganddrug.ui.onboarding.LogoutScreen.LogoutViewModel
import com.medical.buganddrug.ui.onboarding.NavDrawer.bugReportScreen.BugReportViewModel

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogoutScreen(
    viewModel: LogoutViewModel,
    onBackClick: () -> Unit,
    onConfirmLogout: () -> Unit
) {
    val isLoading by viewModel.loading.collectAsState()

    Scaffold(
        topBar = {
            topBar(
                topic = "Log Out",
                patientType = "Carefully review before proceeding",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Warning icon
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFFFFEBEE), shape = RoundedCornerShape(40.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "!",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFC62828)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Are you sure you want to log out?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Disclaimer / important note
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFF3E0)  // Light orange warning bg
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Important Notice",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFEF6C00)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Logging out will end your current session and remove stored data. " +
                                   "This application is intended for licensed medical professionals only. " +
                                   "Ensure all clinical decisions are properly documented before ending your session.\n\n" +
                                   "You will need to sign in again to access patient-related features.",
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 24.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Action buttons
                GradientButton(
                    text = if (isLoading) "Logging out..." else "Yes, Log Out",
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onClick = {
                        viewModel.logout {
                            onConfirmLogout()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onBackClick,
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Cancel",
                        fontSize = 16.sp,
                        color = Color(0xFF800080)
                    )
                }

                Spacer(modifier = Modifier.height(64.dp))
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF800080)
                )
            }
        }
    }
}


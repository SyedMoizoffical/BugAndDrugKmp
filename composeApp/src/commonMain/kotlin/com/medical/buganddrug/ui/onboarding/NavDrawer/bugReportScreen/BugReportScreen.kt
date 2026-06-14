//package com.medical.buganddrug.ui.onboarding.NavDrawer.bugReportScreen
//
//import android.content.Context
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.net.Uri
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.PickVisualMediaRequest
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//
//import androidx.compose.material.icons.outlined.AddPhotoAlternate
//import androidx.compose.material.icons.outlined.Delete
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//import coil.compose.AsyncImage
//import coil.request.ImageRequest
//import com.medical.buganddrug.ui.EtiologicalAgentScreen.EtiologicalAgentScreenViewModel
//import com.medical.buganddrug.ui.QuickIDConsult.topBar
//import com.medical.buganddrug.ui.onboarding.GradientButton
//import com.medical.buganddrug.util.ErrorAlertDialog
//import com.medical.buganddrug.util.LoadingOverlay
//import java.io.ByteArrayOutputStream
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun BugReportScreen(
//    viewModel: BugReportScreenReportViewModel,
//    onBackClick: () -> Unit,
//  //  onSubmit: (description: String, imageUris: List<Uri>) -> Unit // connect to your logic
//) {
//    var description by remember { mutableStateOf("") }
//    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
//    var errorMessage by remember { mutableStateOf<String?>(null) }
//    val context = LocalContext.current
//    val isLoading by viewModel.loading.collectAsState()
//    val apiError by viewModel.errorMessage.collectAsState()
//    val success by viewModel.success.collectAsState()
//    var showSuccessDialog by remember { mutableStateOf(false) }
//    // Photo Picker (gallery) - single or multiple
//    val MAX_IMAGES = 5
//
//    val photoPickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.PickMultipleVisualMedia()
//    ) { uris ->
//
//        if (uris.isNotEmpty()) {
//
//            val newList = (selectedImageUris + uris)
//                .distinctBy { it.toString() }
//                .take(MAX_IMAGES)
//
//            selectedImageUris = newList
//
//            if (newList.size >= MAX_IMAGES) {
//                errorMessage = "Maximum 5 images allowed"
//            }
//        }
//    }
//    if (success) {
//        AlertDialog(
//            onDismissRequest = { onBackClick() },
//            title = { Text("Report Submitted") },
//            text = { Text("Your bug report has been submitted successfully. Thank you for helping us improve the app.") },
//            confirmButton = {
//                TextButton(
//                    onClick = {
//                        onBackClick()
//                    }
//                ) {
//                    Text("OK")
//                }
//            }
//        )
//    }
//    LaunchedEffect(success) {
//        if (success) {
//            showSuccessDialog = true
//            viewModel.clearSuccess()
//        }
//    }
//    // Optional: Camera launcher (requires camera permission + file provider setup)
//    // For simplicity we're using only gallery here — camera can be added similarly
//    if (showSuccessDialog) {
//        AlertDialog(
//            onDismissRequest = {
//
//            },
//            title = { Text("Report Submitted") },
//            text = {
//                Text("Your bug report has been submitted successfully. Thank you for helping us improve the app.")
//            },
//            confirmButton = {
//                TextButton(
//                    onClick = {
//                        showSuccessDialog = false
//                        onBackClick()
//                    }
//                ) {
//                    Text("OK")
//                }
//            }
//        )
//    }
//    Scaffold(
//
//            topBar = {
//                topBar(
//                    topic = "Report a Bug",
//                    patientType = "Report your issue",
//                    onBackClick = onBackClick
//                )
//            }
//
//        ,
//        bottomBar = {
//
//            GradientButton(
//                text = "Submit Report",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                onClick = {
//                    if (description.trim().isBlank()) {
//                        errorMessage = "Please describe the issue"
//                    } else {
//                        viewModel.postBugReport(
//                            context = context,
//                            description = description,
//                            imageUris = selectedImageUris
//                        )
//                    }
//                }
//            )
//
//        }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .padding(16.dp)
//                .verticalScroll(rememberScrollState())
//        ) {
//            Text(
//                text = "Help us improve Bug & Drug",
//                fontSize = 20.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color(0xFF800080)
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Text(
//                text = "Please describe what went wrong and attach screenshots if possible.",
//                fontSize = 14.sp,
//                color = MaterialTheme.colorScheme.onSurfaceVariant
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // Description field
//            OutlinedTextField(
//                value = description,
//                onValueChange = { description = it },
//                label = { Text("Describe the bug / issue") },
//                placeholder = { Text("Steps to reproduce, what you expected, what happened...") },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(160.dp),
//                shape = RoundedCornerShape(12.dp),
//                isError = errorMessage != null,
//                supportingText = {
//                    errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }
//                }
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // Image attachment section
//            Text(
//                text = "Attach screenshots / photos (optional)",
//                fontSize = 16.sp,
//                fontWeight = FontWeight.SemiBold
//            )
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            LazyRow(
//                horizontalArrangement = Arrangement.spacedBy(12.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                // Add image button
//                item {
//                    Surface(
//                        modifier = Modifier
//                            .size(100.dp)
//                            .clip(RoundedCornerShape(12.dp))
//                            .clickable {
//                                photoPickerLauncher.launch(
//                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
//                                )
//                            },
//                        color = MaterialTheme.colorScheme.surfaceVariant,
//                        shape = RoundedCornerShape(12.dp)
//                    ) {
//                        Column(
//                            modifier = Modifier.fillMaxSize(),
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            verticalArrangement = Arrangement.Center
//                        ) {
//                            Icon(
//                                Icons.Outlined.AddPhotoAlternate,
//                                contentDescription = null,
//                                tint = MaterialTheme.colorScheme.primary,
//                                modifier = Modifier.size(36.dp)
//                            )
//                            Spacer(modifier = Modifier.height(4.dp))
//                            Text("Add", fontSize = 12.sp)
//                        }
//                    }
//                }
//
//                // Selected images previews
//                items(selectedImageUris) { uri ->
//                    Box {
//                        AsyncImage(
//                            model = ImageRequest.Builder(LocalContext.current)
//                                .data(uri)
//                                .crossfade(true)
//                                .build(),
//                            contentDescription = "Attached screenshot",
//                            modifier = Modifier
//                                .size(100.dp)
//                                .clip(RoundedCornerShape(12.dp)),
//                            contentScale = ContentScale.Crop
//                        )
//
//                        IconButton(
//                            onClick = {
//                                selectedImageUris = selectedImageUris.filter { it != uri }
//                            },
//                            modifier = Modifier
//                                .align(Alignment.TopEnd)
//                                .size(28.dp)
//                                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(50))
//                        ) {
//                            Icon(
//                                Icons.Outlined.Delete,
//                                contentDescription = "Remove image",
//                                tint = Color.White,
//                                modifier = Modifier.size(18.dp)
//                            )
//                        }
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Optional hint
//            Text(
//                text = "Tip: Include what you were doing, device info, and screenshots help a lot!",
//                fontSize = 12.sp,
//                color = MaterialTheme.colorScheme.onSurfaceVariant,
//                modifier = Modifier.padding(horizontal = 4.dp)
//            )
//
//            Spacer(modifier = Modifier.height(120.dp)) // extra space for bottom button
//        }
//        if (isLoading) {
//            LoadingOverlay()
//        }
//        if (apiError != null) {
//            ErrorAlertDialog(
//                errorMessage = apiError,
//                onDismiss = { viewModel.clearError() }
//            )
//        }
//    }
//
//}
//fun compressImage(context: Context, uri: Uri): ByteArray? {
//
//    return try {
//
//        val inputStream = context.contentResolver.openInputStream(uri)
//        val bitmap = BitmapFactory.decodeStream(inputStream)
//
//        var quality = 100
//        val stream = ByteArrayOutputStream()
//
//        do {
//            stream.reset()
//            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
//            quality -= 5
//        } while (stream.size() > 100 * 1024 && quality > 10)
//
//        stream.toByteArray()
//
//    } catch (e: Exception) {
//        null
//    }
//}
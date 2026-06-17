package com.medical.buganddrug.ui.onboarding.NavDrawer.bugReportScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Close

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.medical.buganddrug.pickImages

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BugReportScreen(
    onBackClick: () -> Unit,
    viewModel: BugReportViewModel
) {
    var description by remember { mutableStateOf("") }
    var selectedImages by remember { mutableStateOf<List<ByteArray>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val isLoading by viewModel.loading.collectAsState()
    val success by viewModel.success.collectAsState()

    if (success) {
        AlertDialog(
            onDismissRequest = { onBackClick() },
            title = { Text("Report Submitted") },
            text = {
                Text("Your bug report has been submitted successfully.")
            },
            confirmButton = {
                TextButton(onClick = onBackClick) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Report a Bug") }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    if (description.isBlank()) {
                        errorMessage = "Please describe the issue"
                    } else {
                        viewModel. postBugReport(
                            description = description,
                            email = "gogle@mail",
                            deviceInfo = "Qmobile",
                            images = selectedImages
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Submit Report")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = "Help us improve",
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Please explain your issue and attach screenshots if needed."
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                    errorMessage = null
                },
                label = { Text("Describe issue") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                isError = errorMessage != null
            )

            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Attach screenshots")

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                item {
                    AddImageBox {
                        pickImages { images ->
                            selectedImages =
                                (selectedImages + images).take(5)
                        }
                    }
                }

                items(selectedImages) { image ->
                    ImagePreview(
                        imageBytes = image,
                        onDelete = {
                            selectedImages =
                                selectedImages.filter { it != image }
                        }
                    )
                }
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
@Composable
fun AddImageBox(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.LightGray)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Outlined.AddCircle,
            contentDescription = null
        )
    }
}
@Composable
fun ImagePreview(
    imageBytes: ByteArray,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier.size(100.dp)
) {
    Box(modifier = modifier) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalPlatformContext.current)  // Works on both platforms
                .data(imageBytes)
                .crossfade(true)
                .build()
        )

        Image(
            painter = painter,
            contentDescription = "Image Preview",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        val state by painter.state.collectAsState()

        // Loading state
        if (state is AsyncImagePainter.State.Loading) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Gray.copy(0.6f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }

        // Error state
        if (state is AsyncImagePainter.State.Error) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text("Failed to load", color = Color.White, fontSize = 12.sp)
            }
        }

        // Delete Button
        IconButton(
            onClick = onDelete,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .background(Color.Black.copy(alpha = 0.7f), CircleShape)
        ) {
            Icon(
                Icons.Outlined.Close,
                contentDescription = "Delete",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

package com.medical.buganddrug.ui.postExposureProphylaxis

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items



import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import buganddrug_multiplateform.composeapp.generated.resources.Res
import buganddrug_multiplateform.composeapp.generated.resources.arrow_drop_down
import buganddrug_multiplateform.composeapp.generated.resources.info

import com.medical.buganddrug.data.model.QoestionsModel.Q4Model.Isolation
import com.medical.buganddrug.data.model.postExosureProplaxisModel.ExposureProPhylaxis
import com.medical.buganddrug.ui.QuickIDConsult.topBar
import com.medical.buganddrug.ui.theme.cardLightBackgroundColor
import com.medical.buganddrug.util.ErrorAlertDialog
import com.medical.buganddrug.util.LoadingOverlay
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

@Composable
fun PostExposureProphylaxisScreen(
    viewModel: PostExposureProphylaxisViewModel,
    onSubmit: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) { viewModel.getExposureProPhylaxisModel() }

    val response = viewModel.getPrecautionFinderList
    val isLoading by viewModel.loading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var selectedConditionId by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            topBar(
                topic = "Post-Exposure Prophylaxis",
                patientType = "Select Post-Exposure Prophylaxis",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {

            when {
                isLoading -> {
                    LoadingOverlay()                }
                errorMessage != null -> {
                    ErrorAlertDialog(
                        errorMessage = errorMessage,
                        onDismiss = { viewModel.clearError() }
                    )
                }
                response != null -> {
                    val isolations = response.exposureProPhylaxisList

                    // Build dropdown list from API
                    val conditions = isolations.map { it.infection to it.id }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(innerPadding)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Dropdown
                        SingleSelectSearchableSpinnerDialog(
                            label = "Select Infection",
                            items = conditions,
                            itemLabel = { it.first },
                            selectedItem = selectedConditionId,
                            onItemSelected = { item -> selectedConditionId = item?.second }
                        )

                        // Details card for selected condition
                        val selectedIsolation =
                            isolations.find { it.id == selectedConditionId }

                        if (selectedIsolation != null) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.outline,
                                        MaterialTheme.shapes.medium
                                    ),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFFEEDCFF))
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            painter = painterResource(Res.drawable.info),
                                            contentDescription = "Table Icon",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            text = "Infection Details for ${selectedIsolation.infection}",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    Spacer(Modifier.height(12.dp))

                                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                        item {
                                            IsolationDetailCard(selectedIsolation)
                                        }
                                    }
                                }
                            }
                        }

                        // Submit
//                        Button(
//                            onClick = { onSubmit() },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(top = 12.dp),
//                            enabled = selectedConditionId != null
//                        ) {
//                            Text("Submit")
//                        }
                    }
                }
            }
        }
    }
}
@Composable
fun IsolationDetailCard(option: ExposureProPhylaxis) {
    Card(
        modifier = Modifier
            .width(330.dp)
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    )  {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InfoRow("Definition of Exposure", option.definitionofExposure)
            InfoRow("PostExposure Risk Assessment", option.postExposureRiskAssessment)
            InfoRow("Post Exposure Prophylaxis", option.postExposureProphylaxis)
            InfoRow("Follow Up", option.followUp)
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column {
        Text(
            label,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            value,
            color = MaterialTheme.colorScheme.onSurface
        )
        Divider(
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 0.5.dp,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}


@Composable
fun <T> SingleSelectSearchableSpinnerDialog(
    label: String,
    items: List<Pair<String, T>>,
    itemLabel: (Pair<String, T>) -> String,
    selectedItem: T?,
    onItemSelected: (Pair<String, T>?) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var searchText by remember {
        mutableStateOf(
            items.find { it.second == selectedItem }?.let(itemLabel) ?: ""
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { showDialog = true }
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = MaterialTheme.shapes.medium,
            trailingIcon = {
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        painter = painterResource(Res.drawable.arrow_drop_down),
                        contentDescription = "Select"
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = LocalContentColor.current.copy(alpha = 1f),
                disabledLabelColor = LocalContentColor.current.copy(alpha = 1f),
                disabledTrailingIconColor = LocalContentColor.current.copy(alpha = 1f),
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledContainerColor = Color.White
            )
        )
    }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp,
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    var searchQuery by remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Search $label") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val filteredItems = (if (searchQuery.isEmpty()) {
    items
} else {
    items.filter { itemLabel(it).contains(searchQuery, ignoreCase = true) }
}).sortedBy { itemLabel(it).trim().lowercase() }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp)
                    ) {
                        items(filteredItems) { item ->
                            TextButton(
                                onClick = {
                                    searchText = itemLabel(item)
                                    onItemSelected(item)
                                    showDialog = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = itemLabel(item).trim(),
                                    textAlign = TextAlign.Left,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuestionFourScreenPreview() {
    val questionViewModel: PostExposureProphylaxisViewModel = koinInject()

    MaterialTheme {
        PostExposureProphylaxisScreen(
            questionViewModel,
            onSubmit = {},
            onBackClick = {}
        )
    }
}
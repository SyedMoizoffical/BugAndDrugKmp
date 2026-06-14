package com.medical.buganddrug.ui.patientInfoScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll



import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

import com.medical.buganddrug.data.model.QoestionsModel.Q2Model.ClinicalSyndromeDiseaseIdentificationLists
import com.medical.buganddrug.ui.QuickIDConsult.Q2.QuestionTwoViewModel
import com.medical.buganddrug.ui.QuickIDConsult.topBar
import com.medical.buganddrug.util.*

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import buganddrug_multiplateform.composeapp.generated.resources.Res
import buganddrug_multiplateform.composeapp.generated.resources.arrow_drop_down
import buganddrug_multiplateform.composeapp.generated.resources.info
import com.medical.buganddrug.data.model.QoestionsModel.Q2Model.DiseaseIdentificationLists
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.JsonArray
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun QuestionTwoScreen(
    viewModel: QuestionTwoViewModel,
    onSubmit: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) { viewModel.getQ2Data() }

    // State
    var selectedSymptomIds by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedLocalizationId by remember { mutableStateOf<Int?>(null) }
    var selectedLevel1Id by remember { mutableStateOf<Int?>(null) }
    var selectedLevel2Id by remember { mutableStateOf<Int?>(null) }
    var noOfDays by remember { mutableStateOf<String>("") }
    var matchedDiseases by remember { mutableStateOf<List<DiseaseIdentificationLists>>(emptyList()) }
    var selectedItemsJsonArray by remember {
        mutableStateOf<JsonArray>(buildJsonArray { })
    }
    var showCard by remember { mutableStateOf(false) }
    var symptomExtraInputs by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    val indWindingData by viewModel.indWindingData.collectAsState()
    var isFever by remember { mutableStateOf(true) } // true = Fever (0), false = Not Fever (1)
    val feverId = if (isFever) 1 else 0
    // Response data
    val response = viewModel.getSyndromeIdentificationData?.diseaseIdenticifationlists
    val isLoading by viewModel.loading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val symptoms = response?.distinctBy { it.symptomId }?.map { it.symptomName to it.symptomId } ?: emptyList()
    val localizations = remember(noOfDays, isFever) {

        val days = noOfDays.toIntOrNull() ?: 0
        val compareValue = if (days in 1..14) 14 else 15

        response
            ?.filter {
                (!isFever || it.durationofdays == compareValue) &&
                        it.symptomId.toInt() == feverId
            }
            ?.distinctBy { it.localizationId }
            ?.map { it.localization to it.localizationId }
            ?: emptyList()
    }
    val days = noOfDays?.toIntOrNull() ?: 0
    val compareValue = if (days in 1..14) 14 else 15

    val level1Classifications = remember(selectedLocalizationId, noOfDays, isFever) {
        response?.filter {
            it.localizationId == selectedLocalizationId &&
                    (!isFever || it.durationofdays == compareValue) &&
                    it.symptomId.toInt() == feverId
        }?.distinctBy { it.level1classificationId }
            ?.map { it.level1classification to it.level1classificationId }
            ?: emptyList()
    }

    val level2Classifications = remember(selectedLocalizationId, selectedLevel1Id, noOfDays, isFever) {
        response?.filter {
            it.localizationId == selectedLocalizationId &&
                    it.level1classificationId == selectedLevel1Id &&
                    it.level2classification != "Not Required" &&
                    (!isFever || it.durationofdays == compareValue) &&
                    it.symptomId.toInt() == feverId
        }?.distinctBy { it.level2classificationId }
            ?.map { it.level2classification to it.level2classificationId }
            ?: emptyList()
    }

    val filteredDiseases = remember(
        selectedLocalizationId,
        selectedLevel1Id,
        selectedLevel2Id,
        noOfDays,
        isFever
    ) {
        response?.filter { data ->
            data.localizationId == selectedLocalizationId &&
                    data.level1classificationId == selectedLevel1Id &&
                    (selectedLevel2Id == null || data.level2classificationId == selectedLevel2Id) &&
                    (!isFever || data.durationofdays == compareValue) &&
                    data.symptomId.toInt() == feverId
        } ?: emptyList()
    }

    Scaffold(
        topBar = {
            topBar(
                topic = "Syndromes Identification",
                patientType = "Select symptoms & classifications",
                onBackClick = onBackClick
            )
        }
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
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // ========== Dropdowns Section ==========
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.elevatedCardElevation(3.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (isFever) "Fever" else "No Fever",
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Switch(
                                checked = isFever,
                                onCheckedChange = { isChecked ->
                                    isFever = isChecked
                                    //
                                    selectedLocalizationId = null
                                    selectedLevel1Id = null
                                    selectedLevel2Id = null
                                    matchedDiseases = emptyList()
                                    noOfDays=""
                                }
                            )
                        }
                        OutlinedTextField(

                            value = noOfDays,
                            onValueChange = {
                                noOfDays = it
                                selectedLocalizationId = null
                                selectedLevel1Id = null
                                selectedLevel2Id = null
                                matchedDiseases = emptyList()
                                            },
                            label = { Text("Duration of illness in days") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
//                        MultiSelectInputSearchableSpinnerDialog(
//                            label = "Symptoms",
//                            items = symptoms,
//                            itemId = { it.second },
//                            itemLabel = { it.first },
//                            selectedIds = selectedSymptomIds,
//                            onSelectionChanged = { ids, labels, extras ->
//                                selectedSymptomIds = ids
//                                symptomExtraInputs = extras
//                                selectedLocalizationId = null
//                                selectedLevel1Id = null
//                                selectedLevel2Id = null
//                                matchedDiseases = emptyList()
//                                showCard = false
//
//                                val symptomsJsonArray = JSONArray().apply {
//                                    ids.forEachIndexed { index, id ->
//                                        noOfDays=extras[id]
//                                        put(
//                                            JSONObject().apply {
//                                                put("id", id)
//                                                put("label", labels.getOrNull(index) ?: "none")
//                                                put("noOfDays", extras[id] ?: "0")
//                                            }
//                                        )
//                                    }
//                                }
//                                println("👉 Selected Symptoms: $symptomsJsonArray")
//                            }
//                        )

                        if (noOfDays!!.isNotEmpty() && localizations.isNotEmpty()) {
                            SingleSelectSearchableSpinnerDialog(
                                label = "Localization",
                                items = localizations,
                                itemLabel = { it.first },
                                selectedItem = selectedLocalizationId,
                                onItemSelected = {
                                    selectedLocalizationId = it?.second
                                    selectedLevel1Id = null
                                    selectedLevel2Id = null
                                    matchedDiseases = emptyList()
                                    showCard = false
                                }
                            )
                        }

                        if (selectedLocalizationId != null && level1Classifications.isNotEmpty()) {
                            SingleSelectSearchableSpinnerDialog(
                                label = "Level 1 Classification",
                                items = level1Classifications,
                                itemLabel = { it.first },
                                selectedItem = selectedLevel1Id,
                                onItemSelected = {
                                    selectedLevel1Id = it?.second
                                    selectedLevel2Id = null
                                    matchedDiseases = emptyList()
                                    showCard = false
                                }
                            )
                        }

                        if (selectedLevel1Id != null && level2Classifications.isNotEmpty()) {
                            SingleSelectSearchableSpinnerDialog(
                                label = "Level 2 Classification",
                                items = level2Classifications,
                                itemLabel = { it.first },
                                selectedItem = selectedLevel2Id,
                                onItemSelected = {
                                    selectedLevel2Id = it?.second
                                    matchedDiseases = emptyList()
                                    showCard = false
                                }
                            )
                        }
                    }
                }

                // ========== Disease Report Card ==========
                if (showCard && matchedDiseases.isNotEmpty()) {
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.elevatedCardElevation(6.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.info),
                                    contentDescription = "Report Icon",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Diagnostic Report",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            val now = Clock.System.now()
                                .toLocalDateTime(TimeZone.currentSystemDefault())

                            val formattedDate =
                                "${now.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)} " +
                                        "${now.dayOfMonth.toString().padStart(2, '0')}, " +
                                        "${now.year}, " +
                                        "${(if (now.hour % 12 == 0) 12 else now.hour % 12)
                                            .toString()
                                            .padStart(2, '0')}:" +
                                        "${now.minute.toString().padStart(2, '0')} " +
                                        if (now.hour < 12) "AM" else "PM"
                            Text(
                                text = "Generated: $formattedDate",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            val groupedDiseases = matchedDiseases.groupBy { it.diseaseId }

                            Text(
                                text = "Suggested Diseases and Recommended Tests",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 220.dp)
                            ) {
                                items(groupedDiseases.entries.toList()) { (diseaseId, diseases) ->

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 6.dp)
                                    ) {
//                                        Text(
//                                            text = "$diseaseName  (Days: $noOfDays)",
//                                            style = MaterialTheme.typography.bodyMedium.copy(
//                                                fontWeight = FontWeight.Medium
//                                            )
//                                        )
//                                        Spacer(modifier = Modifier.height(4.dp))

                                        diseases
                                            .groupBy { it.disease }
                                            .forEach { (diseaseName, items) ->

                                                val testNames = items
                                                    .mapNotNull { it.testName }
                                                    .distinct()
                                                    .joinToString("\n") // 👈 line break instead of comma

                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 6.dp)
                                                ) {
                                                    // Disease Name
                                                    Text(
                                                        text = diseaseName,
                                                        style = MaterialTheme.typography.bodyMedium.copy(
                                                            fontWeight = FontWeight.Medium
                                                        )
                                                    )

                                                    Spacer(modifier = Modifier.height(2.dp))

                                                    // Test Names (each on new line)
                                                    Text(
                                                        text = testNames,
                                                        style = MaterialTheme.typography.bodyMedium
                                                    )

                                                    Divider(
                                                        color = MaterialTheme.colorScheme.outlineVariant,
                                                        thickness = 0.5.dp,
                                                        modifier = Modifier.padding(top = 6.dp)
                                                    )
                                                }
                                            }                                }
                                }
                            }

//                            Spacer(modifier = Modifier.height(8.dp))
//
//                            if (!indWindingData.isNullOrEmpty()) {
//                                Text(
//                                    text = "Indwelling:",
//                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
//                                )
//                                Text(text = indWindingData ?: "")
//                            }
                        }
                    }
                }

                // ========== Submit Button ==========
                Button(
                    onClick = {
                        matchedDiseases = filteredDiseases
                        selectedItemsJsonArray = buildJsonArray {
                            matchedDiseases.forEach { data ->
                                add(
                                    buildJsonObject {
                                        put("symptomId", data.symptomId)
                                        put("localizationId", data.localizationId)
                                        put("level1classificationId", data.level1classificationId)
                                        put("level2classificationId", data.level2classificationId)
                                        put("disease", data.disease)
                                        put("testName", data.testName)
                                        put("noOfDays", symptomExtraInputs[data.symptomId] ?: "0")
                                    }
                                )
                            }
                        }
                        showCard = true
                        println("👉 Selected Items: $selectedItemsJsonArray")
                        onSubmit()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    ),
                    enabled =
                       // selectedSymptomIds.isNotEmpty() &&
                            selectedLocalizationId != null &&
                            selectedLevel1Id != null &&
                            (level2Classifications.isEmpty() || selectedLevel2Id != null) &&
                            filteredDiseases.isNotEmpty()
                ) {
                    Text("Submit", style = MaterialTheme.typography.titleMedium)
                }
            }

            if (isLoading) LoadingOverlay()
            if (errorMessage != null) {
                ErrorAlertDialog(errorMessage = errorMessage, onDismiss = { viewModel.clearError() })
            }
        }
    }
}


@Composable
fun <T> MultiSelectInputSearchableSpinnerDialog(
    label: String,
    items: List<T>,
    itemId: (T) -> String,
    itemLabel: (T) -> String,
    selectedIds: List<String>,
    onSelectionChanged: (selected: List<String>, selectedLabels: List<String>, extraInputs: Map<String, String>) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedItems by remember { mutableStateOf(items.filter { itemId(it) in selectedIds }) }
    var extraInputs by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { showDialog = true }
    ) {
        OutlinedTextField(
            value = selectedItems.joinToString(", ") { itemLabel(it) },
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = MaterialTheme.shapes.small,
            trailingIcon = {
                IconButton(onClick = { showDialog = true }) {
                    Icon(painter = painterResource(Res.drawable.arrow_drop_down), contentDescription = "Select")
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
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                    .background(Color.White) // 👈 Ensure inside is also white
                        .padding(16.dp),

                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    var searchQuery by remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Search $label") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        shape = MaterialTheme.shapes.medium                    )

                    val filteredItems = if (searchQuery.isEmpty()) items
                    else items.filter { itemLabel(it).contains(searchQuery, ignoreCase = true) }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp)
                    ) {
                        items(filteredItems) { item ->
                            val isSelected = selectedItems.any { itemId(it) == itemId(item) }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = { checked ->
                                        val id = itemId(item)
                                        selectedItems = if (checked) {
                                            selectedItems + item
                                        } else {
                                            selectedItems.filter { itemId(it) != id }
                                        }

                                        // Update extraInputs properly (remove entry if unchecked)
                                        extraInputs = if (!checked) {
                                            extraInputs.toMutableMap().apply { remove(id) }
                                        } else {
                                            extraInputs
                                        }
                                    }
                                )
                                Text(
                                    text = itemLabel(item),
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        selectedItems.forEach { item ->
                            val id = itemId(item)
                            val labelText = itemLabel(item)
                            if (!extraInputs.containsKey(id)) {
                                extraInputs = extraInputs.toMutableMap().apply {
                                    this[id] = "1"
                                }
                            }
                            OutlinedTextField(
                                value = extraInputs[id] ?: "1",
                                onValueChange = { newValue ->
                                    extraInputs = extraInputs.toMutableMap().apply {
                                        this[id] = newValue
                                    }
                                },
                                label = { Text("No of days with $labelText") },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number
                                ),
                                maxLines = 1,
                                singleLine = true
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = {
                                onSelectionChanged(
                                    selectedItems.map(itemId),
                                    selectedItems.map(itemLabel),
                                    extraInputs.toMap()
                                )
                                showDialog = false
                            }
                        ) { Text("OK") }
                    }
                }
            }
        }
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
                    Icon(painter = painterResource(Res.drawable.arrow_drop_down)
                        , contentDescription = "Select")
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
                        .background(Color.White)
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
                                    text = itemLabel(item),
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
fun QuestionTwoScreenPreview() {
    MaterialTheme {
        val questionViewModel: QuestionTwoViewModel = koinInject()

        QuestionTwoScreen(
            viewModel = questionViewModel,
            onSubmit = {}
        )
    }
}
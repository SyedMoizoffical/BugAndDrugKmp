package com.medical.buganddrug.ui.CultureGuideTherapy

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import com.medical.buganddrug.ui.QuickIDConsult.Q4.QuestionFourViewModel
import com.medical.buganddrug.ui.QuickIDConsult.topBar
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.sp
import buganddrug_multiplateform.composeapp.generated.resources.Res
import buganddrug_multiplateform.composeapp.generated.resources.arrow_drop_down
import buganddrug_multiplateform.composeapp.generated.resources.info
import com.medical.buganddrug.data.model.CultureTherapyGuideModel.CultureTherapyGuideDto
import com.medical.buganddrug.util.ErrorAlertDialog
import com.medical.buganddrug.util.LoadingOverlay
import org.jetbrains.compose.resources.painterResource
import kotlinx.serialization.Serializable

// ---------- DATA MODELS ----------
@Serializable
data class AntibioticRecord(
    val specimen: String,
    val infection: String,
    val organism: String,
    val antibiotic: String,
    val priority: Int,
    val comment: String
)

@Serializable
data class SelectedAntibiotic(
    val antibiotic: String,
    val result: String
)



@Composable
fun CultureGuideTherapyScreen(
    viewModel: CultureGuideViewModel,
    onSubmit: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val cultureGuideList by viewModel.cultureGuideList.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var selectedSpecimen by remember { mutableStateOf<String?>(null) }
    var selectedInfection by remember { mutableStateOf<String?>(null) }
    var selectedOrganism by remember { mutableStateOf<String?>(null) }

    var selectedValues by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var finalResults by remember { mutableStateOf<List<SelectedAntibiotic>>(emptyList()) }

    // ---------- DUMMY DATA ----------

    val dummyAntibioticData = listOf(
        AntibioticRecord("Urine", "Cystitis", "E. coli", "Nitrofurantoin", 1, "Preferred"),
        AntibioticRecord("Urine", "Cystitis", "E. coli", "Ciprofloxacin", 3, "High resistance"),
        AntibioticRecord("Urine", "Cystitis", "E. coli", "Ceftriaxone", 2, ""),
        AntibioticRecord("Urine", "Pyelonephritis", "Klebsiella", "Piperacillin-tazobactam", 1, "Preferred for severe"),
        AntibioticRecord("Urine", "Pyelonephritis", "Klebsiella", "Cefepime", 2, ""),
        AntibioticRecord("Wound", "Cellulitis", "S. aureus", "Linezolid", 1, "MRSA activity"),
        AntibioticRecord("Wound", "Cellulitis", "S. aureus", "Clindamycin", 2, "")
    )
    LaunchedEffect(Unit) {
        viewModel.getCultureGuideData()
    }

    val specimenList = cultureGuideList.map { it.diagnosis }.distinct()
    val infectionList = cultureGuideList.filter { it.diagnosis == selectedSpecimen }.map { it.sample }.distinct()
    val organismList =
        cultureGuideList.filter { it.diagnosis == selectedSpecimen && it.sample == selectedInfection }
            .map { it.organism }.distinct()
    val antibioticList = cultureGuideList.filter {
        it.diagnosis == selectedSpecimen &&
                it.sample == selectedInfection &&
                it.organism == selectedOrganism
    }

    Scaffold(
        topBar = {
            topBar(
                topic = "Culture Guided Therapy",
                patientType = "Stepwise selection based on Specimen → Infection → Organism",
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
                            Color(0xFFF3E5F5)
                        )
                    )
                )
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // --- HEADER SECTION ---
                item {
                    Text(
                        text = "Step 1 — Select Specimen",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF800080)
                        ),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                // --- SPECIMEN ---
                item {

                        SingleSelectSearchableSpinnerDialog(
                            label = "Select Specimen",
                            items = specimenList.map { it to it },
                            itemLabel = { it.first },
                            selectedItem = selectedSpecimen,
                            onItemSelected = {
                                selectedSpecimen = it?.second
                                selectedInfection = null
                                selectedOrganism = null
                                selectedValues = emptyMap()
                                finalResults = emptyList()
                            }
                        )

                }

                if (selectedSpecimen != null) {
                    item {
                        Text(
                            text = "Step 2 — Select Infection",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF800080)
                            )
                        )
                    }

                    // --- INFECTION ---
                    item {
                            SingleSelectSearchableSpinnerDialog(
                                label = "Select Infection",
                                items = infectionList.map { it to it },
                                itemLabel = { it.first },
                                selectedItem = selectedInfection,
                                onItemSelected = {
                                    selectedInfection = it?.second
                                    selectedOrganism = null
                                    selectedValues = emptyMap()
                                    finalResults = emptyList()
                                }
                            )

                    }
                }

                if (selectedInfection != null) {
                    item {
                        Text(
                            text = "Step 3 — Select Organism",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF800080)
                            )
                        )
                    }

                    // --- ORGANISM ---
                    item {
                            SingleSelectSearchableSpinnerDialog(
                                label = "Select Organism",
                                items = organismList.map { it to it },
                                itemLabel = { it.first },
                                selectedItem = selectedOrganism,
                                onItemSelected = {
                                    selectedOrganism = it?.second
                                    selectedValues = emptyMap()
                                    finalResults = emptyList()
                                }
                            )

                    }
                }

                // ===================================================================
                //                           ANTIBIOTICS LIST
                // ===================================================================

                if (selectedOrganism != null) {
                    item {
                        Text(
                            text = "Recommended Antibiotics",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF800080)
                            )
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "Tap S/R to mark sensitivity",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF6B7A99)
                        )
                    }

                    items(antibioticList) { record ->
                        AntibioticCard(record, selectedValues) { antibiotic, result ->
                            selectedValues = selectedValues.toMutableMap().apply {
                                put(antibiotic, result)
                            }
                        }
                    }
                }

                // ===================================================================
                //                           SELECTED TABLE
                // ===================================================================

                if (finalResults.isNotEmpty()) {
                    item {
                        SelectedAntibioticTable(finalResults, antibioticList)
                    }
                }

                // ===================================================================
                //                           SUBMIT BUTTON
                // ===================================================================

                if (selectedOrganism != null) {
                    item {
                        Button(
                            onClick = {
                                val selectedSensitive = antibioticList
                                    .filter { selectedValues[it.antibiotic] == "S" }
                                    .sortedBy { it.duration }
                                    .map { SelectedAntibiotic(it.antibiotic, "S") }

                                finalResults = selectedSensitive
                                onSubmit()
                            },
                            enabled = selectedValues.isNotEmpty(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Submit", fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
            if (loading) {
                LoadingOverlay()
            }
            if (errorMessage != null) {
                ErrorAlertDialog(
                    errorMessage = errorMessage,
                    onDismiss = { viewModel.clearError() }
                )
            }

        }
    }
}



@Composable
fun AntibioticCard(
    record: CultureTherapyGuideDto,
    selectedValues: Map<String, String>,
    onSelect: (String, String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(Color.White, Color(0xFFF5F9FF))
                    )
                )
                .padding(16.dp)
        ) {

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {
                    Text(
                        record.antibiotic,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF800080)
                        )
                    )

//                    if (record.notes.isNotEmpty()) {
//                        Text(
//                            record.notes,
//                            style = MaterialTheme.typography.bodySmall,
//                            color = Color(0xFF6B7A8E)
//                        )
//                    }
                }

            }

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(28.dp)) {
                listOf("S", "R").forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            onSelect(record.antibiotic, option)
                        }
                    ) {
                        RadioButton(
                            selected = selectedValues[record.antibiotic] == option,
                            onClick = { onSelect(record.antibiotic, option) }
                        )
                        Text(option)
                    }
                }
            }
        }
    }
}
@Composable
fun SelectedAntibioticTable(
    finalResults: List<SelectedAntibiotic>,
    antibioticList: List<CultureTherapyGuideDto>
) {
    Spacer(Modifier.height(18.dp))
    Text(
        "Selected Antibiotics",
        style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            color = Color(0xFF800080)
        )
    )
    Spacer(Modifier.height(8.dp))
    // 🔎 Check if any record contains the specific note
    val hasNoRespSymptoms = finalResults.any { result ->
        antibioticList.find { it.antibiotic == result.antibiotic }
            ?.sample
            ?.contains("No respiratory tract symptoms") == true
    }

    if (hasNoRespSymptoms){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(Res.drawable.info),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Could be colonizer. Consult ID. Treatment not recommended",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    fontSize = 16.sp,
                    lineHeight = 22.sp
                )
            }
        }
    }else{
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column {

                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFFF3E5F5), Color.White)
                            )
                        )
                        .padding(vertical = 10.dp)
                ) {
                    HeaderCell("Antibiotic", 0.25f)
                    HeaderCell("Preference order", 0.3f)
                    HeaderCell("Comment", 0.4f)
                }

                // Rows
                var serialNo=0
                finalResults.forEachIndexed { index, result ->
                    val record = antibioticList.find { it.antibiotic == result.antibiotic }
                    record ?: return@forEachIndexed
                    val bgColor = if (index % 2 == 0) Color(0xFFF9FBFF) else Color.White

                    if (record.duration!=0){
                        serialNo += 1   // start from 1

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(bgColor)
                                .padding(vertical = 10.dp)
                        ) {
                            RowCell(record.antibiotic, 0.35f)
                            RowCell(serialNo.toString(), 0.2f)
                            RowCell(if (record.notes.isEmpty()) "-" else record.notes, 0.4f)
                        }
                    }

                }
            }
        }
    }


    Spacer(Modifier.height(12.dp))
}


@Composable
fun RowScope.HeaderCell(text: String, weight: Float) {
    Text(
        text = text,
        modifier = Modifier
            .weight(weight)
            .padding(horizontal = 8.dp),
        style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E)
        )
    )
}

@Composable
fun RowScope.RowCell(text: String, weight: Float) {
    Text(
        text = text,
        modifier = Modifier
            .weight(weight)
            .padding(horizontal = 8.dp),
        style = MaterialTheme.typography.bodyMedium
    )
}
@Composable
fun PriorityBadge(priority: Int) {
    val (bg, textColor, label) = when (priority) {
        1 -> Triple(Color(0xFFDCFCE7), Color(0xFF166534), "High")
        2 -> Triple(Color(0xFFFFF7D6), Color(0xFF8A6D1A), "Medium")
        else -> Triple(Color(0xFFFFE4E6), Color(0xFFB91C1C), "Low")
    }

    Box(
        modifier = Modifier
            .background(bg, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

/* SAME SPINNER AS YOUR Q4 SCREEN */
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

                        contentDescription = "Select")
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
                color = Color.White   // 👈 Add this line

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


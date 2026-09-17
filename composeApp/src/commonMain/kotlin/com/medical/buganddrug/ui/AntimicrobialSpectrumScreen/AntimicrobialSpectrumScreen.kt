package com.medical.buganddrug.ui.antimicrobial

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import buganddrug_multiplateform.composeapp.generated.resources.Res
import buganddrug_multiplateform.composeapp.generated.resources.search
import com.medical.buganddrug.data.model.AntimicrobialSpectrumData.BacteriaRow
import com.medical.buganddrug.data.model.LocalStorageDatamodel.GridLists
import com.medical.buganddrug.ui.AntimicrobialSpectrumScreen.AntimicrobialSpectrumViewModel
import com.medical.buganddrug.ui.QuickIDConsult.Q1.QuestionViewModel
import com.medical.buganddrug.ui.QuickIDConsult.topBar
import com.medical.buganddrug.util.ErrorAlertDialog
import com.medical.buganddrug.util.LoadingOverlay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
fun AntimicrobialSpectrumScreen(
    viewModel: AntimicrobialSpectrumViewModel,
    onBackClick: () -> Unit = {},
    patientType: String,
    onSubmit: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        viewModel.getAntimicrobialSpectrumData()
    }
    val response = viewModel.getSyndromeIdentificationData
    val isLoading by viewModel.loading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val verticalScroll = rememberScrollState()
    val horizontalScroll = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    val focusManager = LocalFocusManager.current

    var searchQuery by remember { mutableStateOf("") }
    var highlightedOrganismIndex by remember { mutableStateOf<Int?>(null) }
    var highlightedAntibioticIndex by remember { mutableStateOf<Int?>(null) }

    // Function to return color based on value (Enhanced contrast and accessible palette)
    fun colorForValue(value: Int): Color = when (value) {
        3 -> Color(0xFF10B981) // Emerald Green - Recommended
        2 -> Color(0xFF0284C7) // Sky Blue - Effective
        1 -> Color(0xFFF59E0B) // Amber - Variable
        else -> Color(0xFFEF4444) // Crimson Red - Not effective
    }

    Scaffold(
        topBar = {
            topBar(onBackClick, "Antimicrobial Spectrum", patientType = patientType)
        },
        containerColor = Color.Transparent
    ) { padding ->
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
                .padding(padding)
                .padding(16.dp)
        ) {
            Column {
                // Legend Card
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Interpretation of Scores",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF800080)
                            ),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // 2x2 Grid Layout
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                LegendItem(color = Color(0xFFEF4444), label = "0 = Not effective")
                                LegendItem(color = Color(0xFFF59E0B), label = "1 = May or may not be")
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                LegendItem(color = Color(0xFF0284C7), label = "2 = Effective")
                                LegendItem(color = Color(0xFF10B981), label = "3 = Recommended")
                            }
                        }
                    }
                }

                if (response != null && !isLoading) {
                    val columnHeaders = listOf(
                        "penicillin",
                        "cloxacillin",
                        "ampicillin",
                        "amoxicillin",
                        "amoxicillin Clavulanate",
                        "piperacillin Tazobactam",
                        "cephalexin",
                        "cefazolin",
                        "cefixime",
                        "cefuroxime",
                        "cefpodoxime",
                        "cefotaxime",
                        "cefoperazone",
                        "ceftriaxone",
                        "ceftazidime",
                        "cefepime",
                        "ceftaroline",
                        "cefiderocol",
                        "cefoperazone Sulbactam",
                        "ceftazidime Avibactam",
                        "ertapenem",
                        "imipenem",
                        "meropenem",
                        "aztreonam",
                        "amikacin",
                        "gentamicin",
                        "ciprofloxacin",
                        "levofloxacin",
                        "moxifloxacin",
                        "Trimethoprim Sulfamethoxazole",
                        "vancomycin",
                        "daptomycin",
                        "linezolid",
                        "clindamycin",
                        "doxycycline",
                        "minocycline",
                        "tigecycline",
                        "colistin",
                        "erythromycin",
                        "azithromycin",
                        "clarithromycin",
                        "metronidazole",
                        "nitrofurantoin",
                        "Intravenous Fosfomycin",
                        "Oral Fosfomycin"
                    ).sortedBy { it.lowercase() }

                    // Sort organisms in alphabetical order
                    val sortedGridLists = response.gridLists.sortedBy { (it.Label ?: "").lowercase() }

                    // Row headers
                    val rowHeaders = sortedGridLists.map { it.Label }

                    // Function to scroll to match
                    fun scrollToMatch(query: String) {
                        val q = query.trim().lowercase()
                        if (q.isNotEmpty()) {
                            val orgIdx = rowHeaders.indexOfFirst { (it ?: "").lowercase().contains(q) }
                            val antIdx = columnHeaders.indexOfFirst { it.lowercase().contains(q) }
                            highlightedOrganismIndex = if (orgIdx != -1) orgIdx else null
                            highlightedAntibioticIndex = if (antIdx != -1) antIdx else null
                            coroutineScope.launch {
                                if (orgIdx != -1) {
                                    val rowHeightPx = with(density) { 60.dp.toPx() }
                                    verticalScroll.animateScrollTo((orgIdx * rowHeightPx).toInt())
                                }
                                if (antIdx != -1) {
                                    val colWidthPx = with(density) { 160.dp.toPx() }
                                    horizontalScroll.animateScrollTo((antIdx * colWidthPx).toInt())
                                }
                            }
                        } else {
                            highlightedOrganismIndex = null
                            highlightedAntibioticIndex = null
                        }
                    }

                    // Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { query ->
                            searchQuery = query
                            scrollToMatch(query)
                        },
                        placeholder = {
                            Text(
                                "Search Organism or Antibiotic...",
                                fontSize = 13.sp,
                                color = Color(0xFF94A3B8)
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(Res.drawable.search),
                                contentDescription = "Search",
                                tint = Color(0xFF800080),
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = {
                                    searchQuery = ""
                                    scrollToMatch("")
                                }) {
                                    Text("✕", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            focusManager.clearFocus()
                            scrollToMatch(searchQuery)
                        }),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color(0xFF800080),
                            unfocusedBorderColor = Color(0xFFE2E8F0)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                    )

                    // Matching result chips
                    val matchingOrganisms = if (searchQuery.isNotBlank()) {
                        rowHeaders.mapIndexedNotNull { index, name ->
                            if (name != null && name.contains(searchQuery, ignoreCase = true)) index to name else null
                        }
                    } else emptyList()

                    val matchingAntibiotics = if (searchQuery.isNotBlank()) {
                        columnHeaders.mapIndexedNotNull { index, name ->
                            if (name.contains(searchQuery, ignoreCase = true)) index to name else null
                        }
                    } else emptyList()

                    if (matchingOrganisms.isNotEmpty() || matchingAntibiotics.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            matchingOrganisms.take(6).forEach { (idx, name) ->
                                AssistChip(
                                    onClick = {
                                        highlightedOrganismIndex = idx
                                        highlightedAntibioticIndex = null
                                        focusManager.clearFocus()
                                        coroutineScope.launch {
                                            val rowHeightPx = with(density) { 60.dp.toPx() }
                                            verticalScroll.animateScrollTo((idx * rowHeightPx).toInt())
                                        }
                                    },
                                    label = { Text("🦠 $name", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = if (highlightedOrganismIndex == idx) Color(0xFFF3E8FF) else Color.White
                                    ),
                                    border = BorderStroke(
                                        1.dp,
                                        if (highlightedOrganismIndex == idx) Color(0xFF800080) else Color(0xFFE2E8F0)
                                    )
                                )
                            }

                            matchingAntibiotics.take(6).forEach { (idx, name) ->
                                val displayName = name.lowercase().split(" ").joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
                                AssistChip(
                                    onClick = {
                                        highlightedAntibioticIndex = idx
                                        highlightedOrganismIndex = null
                                        focusManager.clearFocus()
                                        coroutineScope.launch {
                                            val colWidthPx = with(density) { 160.dp.toPx() }
                                            horizontalScroll.animateScrollTo((idx * colWidthPx).toInt())
                                        }
                                    },
                                    label = { Text("💊 $displayName", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = if (highlightedAntibioticIndex == idx) Color(0xFFF3E8FF) else Color.White
                                    ),
                                    border = BorderStroke(
                                        1.dp,
                                        if (highlightedAntibioticIndex == idx) Color(0xFF800080) else Color(0xFFE2E8F0)
                                    )
                                )
                            }
                        }
                    }

                    // Table matrix
                    val data: List<List<Int?>> =
                        sortedGridLists.map { row ->
                            columnHeaders.map { column ->
                                row.valueForColumn(column)
                            }
                        }

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White, RoundedCornerShape(16.dp))
                            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp))
                            .padding(8.dp)
                    ) {
                        // Left column label "Organisms"
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(120.dp)
                        ) {
                            Text(
                                text = "Organisms",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF800080)
                                ),
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .align(Alignment.CenterHorizontally)
                            )

                            Column(
                                modifier = Modifier
                                    .verticalScroll(verticalScroll)
                                    .padding(0.dp, 40.dp, 0.dp, 0.dp)
                            ) {
                                rowHeaders.forEachIndexed { index, rowHeader ->
                                    val isHighlighted = highlightedOrganismIndex == index

                                    Box(
                                        modifier = Modifier
                                            .height(60.dp)
                                            .fillMaxWidth()
                                            .background(
                                                if (isHighlighted) Color(0xFF800080).copy(alpha = 0.3f)
                                                else Color(0xFF800080).copy(alpha = 0.1f)
                                            )
                                            .border(
                                                width = if (isHighlighted) 2.dp else 0.5.dp,
                                                color = if (isHighlighted) Color(0xFF800080) else Color.LightGray
                                            )
                                            .padding(start = 8.dp),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Text(
                                            text = rowHeader ?: "",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontWeight = if (isHighlighted) FontWeight.ExtraBold else FontWeight.Bold,
                                                color = if (isHighlighted) Color(0xFF581C87) else Color.Black,
                                                fontSize = 11.sp
                                            ),
                                            textAlign = TextAlign.Start
                                        )
                                    }
                                }
                            }
                        }

                        // Scrollable data section
                        Column(
                            modifier = Modifier
                                .horizontalScroll(horizontalScroll)
                                .fillMaxHeight()
                        ) {
                            // Top label for antibiotics
                            Text(
                                text = "Antibiotics",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF800080)
                                ),
                                modifier = Modifier
                                    .height(24.dp)
                                    .padding(start = 8.dp)
                            )

                            // Column headers
                            Row {
                                columnHeaders.forEachIndexed { colIndex, header ->
                                    val isHighlighted = highlightedAntibioticIndex == colIndex

                                    Box(
                                        modifier = Modifier
                                            .width(160.dp)
                                            .height(40.dp)
                                            .background(
                                                if (isHighlighted) Color(0xFF4A0E4E)
                                                else Color(0xFF800080)
                                            )
                                            .border(
                                                width = if (isHighlighted) 2.dp else 0.5.dp,
                                                color = if (isHighlighted) Color(0xFFFFD700) else Color.White
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = header
                                                .lowercase()
                                                .split(" ")
                                                .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } },
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = if (isHighlighted) Color(0xFFFFD700) else Color.White,
                                                fontSize = 11.sp
                                            )
                                        )
                                    }
                                }
                            }

                            // Data rows
                            Column(modifier = Modifier.verticalScroll(verticalScroll)) {
                                data.forEachIndexed { rIndex, row ->
                                    val isRowHighlighted = highlightedOrganismIndex == rIndex

                                    Row {
                                        row.forEachIndexed { cIndex, value ->
                                            val cellValue = value ?: 0
                                            val isColHighlighted = highlightedAntibioticIndex == cIndex

                                            Box(
                                                modifier = Modifier
                                                    .width(160.dp)
                                                    .height(60.dp)
                                                    .background(colorForValue(cellValue))
                                                    .border(
                                                        width = if (isRowHighlighted || isColHighlighted) 1.5.dp else 0.5.dp,
                                                        color = if (isRowHighlighted || isColHighlighted) Color(0xFF800080) else Color(0xFFE0E0E0)
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = cellValue.toString(),
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (cellValue == 2) Color.Black else Color.White
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 🔹 LOADER (on top)
            if (isLoading) {
                LoadingOverlay()
            }
            if (errorMessage != null) {
                ErrorAlertDialog(errorMessage = errorMessage, onDismiss = { viewModel.clearError() })
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .width(150.dp)
            .height(40.dp)
            .background(Color(0xFFF9FAFB), RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .background(color, RoundedCornerShape(4.dp))
                .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF1B2B5D))
        )
    }
}

fun GridLists.valueForColumn(column: String): Int? {
    return when (column) {
        "penicillin" -> Penicilling
        "cloxacillin" -> Cloxacillin
        "ampicillin" -> Ampicillin
        "amoxicillin" -> Amoxicillin
        "amoxicillin Clavulanate" -> Amoxicillinclavulanate
        "piperacillin Tazobactam" -> Piperacillintazobactam
        "cephalexin" -> Cephalexin1stgeneration
        "cefazolin" -> Cefazolin1stgeneration
        "cefixime" -> Cefixime2ndgeneration
        "cefuroxime" -> Cefuroxime2ndgeneration
        "cefpodoxime" -> Cefpodoxime3rdgeneration
        "cefotaxime" -> Cefotaxime3rdgeneration
        "cefoperazone" -> Cefoperazone3rdgeneration
        "ceftriaxone" -> Ceftriaxone3rdgeneration
        "ceftazidime" -> Ceftazidime3rdgeneration
        "cefepime" -> Cefepime4thgeneration
        "ceftaroline" -> Ceftaroline5thgeneration
        "cefiderocol" -> Cefiderocol5thgeneration
        "cefoperazone Sulbactam" -> Cefoperazonesulbactam
        "ceftazidime Avibactam" -> CeftazidimeAvibactam
        "ertapenem" -> Ertapenem
        "imipenem" -> Imipenem
        "meropenem" -> Meropenem
        "aztreonam" -> Aztreonam
        "amikacin" -> Amikacin
        "gentamicin" -> gentamicin
        "ciprofloxacin" -> Ciprofloxacin
        "levofloxacin" -> Levofloxacin
        "moxifloxacin" -> Moxifloxacin
        "Trimethoprim Sulfamethoxazole" -> TMPSMX
        "vancomycin" -> Vancomycin
        "daptomycin" -> Daptomycin
        "linezolid" -> Linezolid
        "clindamycin" -> Clindamycin
        "doxycycline" -> Doxycycline
        "minocycline" -> Minocycline
        "tigecycline" -> Tigecycline
        "colistin" -> Colistin
        "erythromycin" -> Erythromycin
        "azithromycin" -> Azithromycin
        "clarithromycin" -> Clarithromycin
        "metronidazole" -> Metronidazole
        "nitrofurantoin" -> Nitrofurantoin
        "Intravenous Fosfomycin" -> FosfomycinIV
        "Oral Fosfomycin" -> FosfomycinPO
        else -> null
    }
}



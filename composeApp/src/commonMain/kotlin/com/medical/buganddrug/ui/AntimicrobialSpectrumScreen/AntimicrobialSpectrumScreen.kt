package com.medical.buganddrug.ui.antimicrobial

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.medical.buganddrug.data.model.AntimicrobialSpectrumData.BacteriaRow
import com.medical.buganddrug.data.model.LocalStorageDatamodel.GridLists
import com.medical.buganddrug.ui.AntimicrobialSpectrumScreen.AntimicrobialSpectrumViewModel
import com.medical.buganddrug.ui.QuickIDConsult.Q1.QuestionViewModel
import com.medical.buganddrug.ui.QuickIDConsult.topBar
import com.medical.buganddrug.util.ErrorAlertDialog
import com.medical.buganddrug.util.LoadingOverlay

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

            //          TopAppBar(title = { Text("qSOFA + NEWS2 Assessment") })
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
                        )                    )
                )
                .padding(padding)
                .padding(16.dp)
        ) {

            Column {
                // Legend Card
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
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

                // Add title for the matrix
                Text(
                    text = "Antimicrobial Activity Matrix",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF800080)
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 8.dp)
                )
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
                    )
// ✅ Row headers
                    val rowHeaders = response!!.gridLists.map{ it.Label }

                    // ✅ Table matrix
                    val data: List<List<Int?>> =
                        response.gridLists.map { row ->
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

                                // Check corresponding data row
                                val isAllZeroRow = data.getOrNull(index)
                                    ?.all { (it ?: 0) == 0 } == true

                                Box(
                                    modifier = Modifier
                                        .height(60.dp)
                                        .fillMaxWidth()
                                        .background(Color(0xFF800080).copy(alpha = 0.1f))
                                        .border(0.5.dp, Color.LightGray),

                                    contentAlignment = Alignment.Center
                                ) {
                                    com.medical.buganddrug.util.ClickableDiseaseText(
                                        text = rowHeader!!,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            textAlign = TextAlign.Center,
                                            color = if (isAllZeroRow)
                                                Color(0xFF800080)   // 👈 Change color if all zero
                                            else
                                                Color.Black
                                        )
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
                            columnHeaders.forEach { header ->
                                Box(
                                    modifier = Modifier
                                        .width(160.dp)
                                        .height(40.dp)
                                        .background(Color(0xFF800080))
                                        .border(0.5.dp, Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    com.medical.buganddrug.util.ClickableDiseaseText(
                                        text = header
                                            .lowercase()
                                            .split(" ")
                                            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } },
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }
                        }

                        // Data rows
                        Column(modifier = Modifier.verticalScroll(verticalScroll)) {
                            data.forEach { row ->

                                // Check if full row contains only 0 (or null treated as 0)
                                val isAllZeroRow = row.all { (it ?: 0) == 0 }

                                Row {
                                    row.forEach { value ->
                                        val cellValue = value ?: 0

                                        Box(
                                            modifier = Modifier
                                                .width(160.dp)
                                                .height(60.dp)
                                                .background(
                                                    if (isAllZeroRow) Color.White
                                                    else colorForValue(cellValue)
                                                )
                                                .border(0.5.dp, Color(0xFFE0E0E0)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = cellValue.toString(),
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isAllZeroRow) Color.White
                                                    else if (cellValue == 2) Color.Black
                                                    else Color.White
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



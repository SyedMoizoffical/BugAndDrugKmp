package com.medical.buganddrug.ui.QuickIDConsult.Q5
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

import com.medical.buganddrug.ui.QuickIDConsult.topBar
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import buganddrug_multiplateform.composeapp.generated.resources.Res
import buganddrug_multiplateform.composeapp.generated.resources.first_aid_kit
import buganddrug_multiplateform.composeapp.generated.resources.info
import com.medical.buganddrug.data.model.QoestionsModel.Q5Model.AntibioticDose
import com.medical.buganddrug.ui.QuickIDConsult.Q3.SingleSelectSearchableSpinnerDialog
import com.medical.buganddrug.ui.theme.cardLightBackgroundColor
import com.medical.buganddrug.util.ErrorAlertDialog
import com.medical.buganddrug.util.LoadingOverlay
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import kotlin.math.round

@Composable
fun QuestionFiveScreen(
    viewModel: QuestionFiveViewModel = koinInject(),
    onSubmit: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var gender by remember { mutableStateOf("Male") }
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var creatinine by remember { mutableStateOf("") }
    var onDialysis by remember { mutableStateOf(false) }
    var crCl by remember { mutableStateOf<Double?>(null) }

    var selectedAntibiotic by remember { mutableStateOf<String?>(null) }
    var adjustedDose by remember { mutableStateOf("") }
    var standardDose by remember { mutableStateOf("") }
    var renalCategory by remember { mutableStateOf<String?>(null) }

    // Fetch API data on first load
    LaunchedEffect(Unit) {
        viewModel.getQ5Data()
    }
    val isLoading by viewModel.loading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Scaffold(
        topBar = {
            topBar(
                topic = "Creatinine Clearance Calculator",
                patientType = "Calculate Renal Function",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Gender Dropdown
            SingleSelectSearchableSpinnerDialog(
                label = "Gender",
                items = listOf("Male" to "Male", "Female" to "Female"),
                itemLabel = { it.first },
                selectedItem = gender,
                onItemSelected = { gender = it?.second ?: "Male" }
            )

            // Age
            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age (years)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Weight
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Weight (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Serum Creatinine
            OutlinedTextField(
                value = creatinine,
                onValueChange = { creatinine = it },
                label = { Text("Serum Creatinine (mg/dL)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Dialysis Switch
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("On Dialysis", modifier = Modifier.weight(1f))
                Switch(checked = onDialysis, onCheckedChange = { onDialysis = it })
            }

            // Calculate Button
            Button(
                onClick = {
                    val a = age.toIntOrNull()
                    val w = weight.toDoubleOrNull()
                    val sc = creatinine.toDoubleOrNull()
                    if (a != null && w != null && sc != null && sc > 0) {
                        var clearance = ((140 - a) * w) / (72 * sc)
                        if (gender == "Female") clearance *= 0.85
                        crCl = clearance

                        // find renal category from API
                        viewModel.renalCategories.find { cat ->
                            val from = cat.eGFRFrom.toDoubleOrNull() ?: Double.MIN_VALUE
                            val to = cat.eGFRTo.toDoubleOrNull() ?: Double.MAX_VALUE
                            clearance >= from && clearance <=to
                        }?.let { renalCategory = it.renalFunctionCategory }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = age.isNotEmpty() && weight.isNotEmpty() && creatinine.isNotEmpty()
            ) {
                Text("Calculate CrCl")
            }

            crCl?.let {
                val formattedCrCl = round(it * 100) / 100.0

                Text("Creatinine Clearance: $formattedCrCl mL/min")

                renalCategory?.let { cat ->
                    Text("Renal Function: $cat")
                }
            }

            Spacer(Modifier.height(12.dp))

            // Antibiotic Dropdown (from API)
            if (crCl != null && viewModel.antibioticDoses.isNotEmpty()) {
                val antibioticList = viewModel.antibioticDoses
                    .map { it.antibioticName }
                    .distinct()

                SingleSelectSearchableSpinnerDialog(
                    label = "Select Antibiotic",
                    items = antibioticList.map { it to it },
                    itemLabel = { it.first },
                    selectedItem = selectedAntibiotic,
                    onItemSelected = { selected ->
                        selectedAntibiotic = selected?.second
                        val dose = getAdjustedDoseFromApi(
                            selectedAntibiotic,
                            crCl!!,
                            onDialysis,
                            viewModel.antibioticDoses
                        )
                        standardDose = dose?.standardDose ?: "N/A"
                        adjustedDose = dose?.adjustedDosing ?: "N/A"
                    }
                )
            }

            // Show results
            if (selectedAntibiotic != null && crCl != null) {
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
                                .background(MaterialTheme.colorScheme.cardLightBackgroundColor)
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.first_aid_kit),
                                contentDescription = "Table Icon",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Dose Details for ${selectedAntibiotic}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Standard Dose:", fontWeight = FontWeight.Bold)
                            Text(standardDose)
                            Divider()

                            Text("Creatinine Clearance:", fontWeight = FontWeight.Bold)

                            val formattedCrCl = round(crCl!! * 100) / 100.0

                            Text("$formattedCrCl mL/min")
                            Divider()

                            renalCategory?.let {
                                Text("Renal Function Category:", fontWeight = FontWeight.Bold)
                                Text(it)
                                Divider()
                            }

                            Text("Renal Adjusted Dose:", fontWeight = FontWeight.Bold)
                            Text(adjustedDose)
                        }
                    }
                }
            }

        }
        if (isLoading) {
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
fun getAdjustedDoseFromApi(
    antibiotic: String?,
    crCl: Double,
    dialysis: Boolean,
    doses: List<AntibioticDose>
): AntibioticDose? {
    if (antibiotic == null) return null

    // Hemodialysis check
    if (dialysis) {
        return doses.find { it.antibioticName == antibiotic && it.creatinineClearanceRange.contains("Hemodialysis", true) }
    }

    return doses.find { it.antibioticName == antibiotic && matchesRange(it.creatinineClearanceRange, crCl) }
}

private fun matchesRange(range: String, crCl: Double): Boolean {

    val r = range.lowercase()
        .replace("crcl", "")
        .replace("≥", ">=")
        .replace("≤", "<=")
        .replace("to", "-")
        .replace("–", "-")
        .replace(" ", "")
        .trim()

    if (r.contains("hemodialysis")) return false

    return when {
        r.startsWith(">=") -> crCl >= r.removePrefix(">=").toDouble()
        r.startsWith("<=") -> crCl <= r.removePrefix("<=").toDouble()
        r.startsWith(">") -> crCl > r.removePrefix(">").toDouble()
        r.startsWith("<") -> crCl < r.removePrefix("<").toDouble()

        r.contains("-") -> {
            val parts = r.split("-")
            val min = parts.getOrNull(0)?.replace(">","")?.toDoubleOrNull()
            val max = parts.getOrNull(1)?.replace("<","")?.toDoubleOrNull()

            when {
                min != null && max != null -> crCl in min..max
                min != null -> crCl > min
                max != null -> crCl < max
                else -> false
            }
        }

        else -> false
    }
}

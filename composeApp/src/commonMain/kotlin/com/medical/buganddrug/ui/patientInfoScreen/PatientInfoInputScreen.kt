package com.medical.buganddrug.ui.patientInfoScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.medical.buganddrug.ui.QuickIDConsult.topBar
import com.medical.buganddrug.ui.onboarding.GradientButton
import com.medical.buganddrug.util.ErrorAlertDialog
import com.medical.buganddrug.util.LoadingOverlay
import com.medical.buganddrug.util.MultiSelectInputSearchableSpinnerDialog
import com.medical.buganddrug.util.MultiSelectSearchableSpinnerDialog
import com.medical.buganddrug.util.SearchableSpinnerDialog
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put


@Composable
fun PatientInfoInputScreen(
    viewModel: UserViewModel,
    onSubmit: () -> Unit = {},
    onBackClick: () -> Unit = {} // ✅ handle back navigation

) {
    LaunchedEffect(Unit) { viewModel.fetchUser() }

    val scrollState = rememberScrollState()

    // Common TextField state
    var age by rememberSaveable { mutableStateOf("") }
    var weight by rememberSaveable { mutableStateOf("") }
    var hr by rememberSaveable { mutableStateOf("") }
    var bpSystolic by rememberSaveable { mutableStateOf("") }
    var bpDiastolic by rememberSaveable { mutableStateOf("") }
    var temp by rememberSaveable { mutableStateOf("") }
    var rr by rememberSaveable { mutableStateOf("") }

    // Checkbox state
    var recentHospitalStay by rememberSaveable { mutableStateOf(false) }
    var immunocompromised by rememberSaveable { mutableStateOf(false) }
    var indwellingCompromised by rememberSaveable { mutableStateOf(false) }
    var selectedIndwellingJsonArray by remember { mutableStateOf(JsonArray(emptyList())) }

    // Dropdown states
    val immunoReasons = viewModel.userState?.immunoReson ?: emptyList()
    val selectedImmunoReason = remember { mutableStateOf(listOf<Int>()) }
    var selectedImmunoReasonJsonArray by remember { mutableStateOf(JsonArray(emptyList())) }

    val symptomsReasons = viewModel.userState?.symptoms ?: emptyList()
    val selectedSymptoms = remember { mutableStateOf(listOf<Int>()) }
    var selectedSymptomsJsonArray by remember { mutableStateOf(JsonArray(emptyList())) }

    val signsReasons = viewModel.userState?.sign ?: emptyList()
    var selectedSign by rememberSaveable { mutableStateOf<Pair<Int?, String?>>(null to null) }
    var selectedSignObj by remember {
        mutableStateOf(JsonObject(emptyMap()))
    }
    val indwellingReasons = viewModel.userState?.indwellingDevices ?: emptyList()
    val selectedIndwelling = remember { mutableStateOf(listOf<Int>()) }

    val diseasesList = viewModel.userState?.diseases ?: emptyList()
    val selecteddiseasesList = remember { mutableStateOf(listOf<Int>()) }
    var selectedDiseasesListJsonArray by remember { mutableStateOf(JsonArray(emptyList())) }


    val isLoading by viewModel.loading.collectAsState()

    val errorMessage by viewModel.errorMessage.collectAsState()

    Scaffold(
        topBar = {
            topBar(
                topic = "Patient Information",
                patientType = "Fill in the details below",
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
                .padding(innerPadding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .imePadding()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Simple reusable text field
                @Composable
                fun patientTextField(
                    value: String,
                    label: String,
                    isNumber: Boolean = true,
                    modifier: Modifier = Modifier, // new
                    onChange: (String) -> Unit
                ) {
                    OutlinedTextField(
                        value = value,
                        onValueChange = onChange,
                        label = { Text(label) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = if (isNumber) KeyboardType.Number else KeyboardType.Text,
                            imeAction = ImeAction.Next // ✅ show "Done" instead of "Next"
                        ),
                        modifier = modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.small,
                        maxLines = 1,   // ✅ restricts to a single line
                        singleLine = true
                    )
                }


                patientTextField(age, "Age", true) { age = it }
                patientTextField(weight, "Weight (kg)", true) { weight = it }


                MultiSelectInputSearchableSpinnerDialog(
                    label = "Symptoms",
                    items = symptomsReasons,
                    itemId =  { it.symptomsID },
                    itemLabel = { it.symptomsName },
                    selectedIds = selectedSymptoms.value,
                    onSelectionChanged = { ids, labels, extras ->
                        selectedSymptomsJsonArray = JsonArray(emptyList()) // reset fresh array

                        ids.forEachIndexed { index, id ->
                            val newObj = buildJsonObject {
                                put("id", id)                                   // String, Int, Long, etc.
                                put("label", labels.getOrNull(index) ?: "none")
                                put("noOfDays", extras[id] ?: "0")              // You can put Int, String, etc.
                            }

                            // Add to JsonArray (immutable, so we create new array)
                            selectedSymptomsJsonArray = JsonArray(
                                selectedSymptomsJsonArray + newObj
                            )
                        }

                        val jsonString = selectedSymptomsJsonArray.toString()
                        println("JSON String: $jsonString")
                    }
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = recentHospitalStay, onCheckedChange = { recentHospitalStay = it })
                    Spacer(Modifier.width(8.dp))
                    Text("Hospital stay for more than 48 hours in last 1 week")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = immunocompromised, onCheckedChange = { immunocompromised = it })
                    Spacer(Modifier.width(8.dp))
                    Text("Immunocompromised")
                }

                if (immunocompromised) {
                    MultiSelectSearchableSpinnerDialog(
                        label = "Immunocompromised",
                        items = immunoReasons,
                        itemId = { it.reasonId },
                        itemLabel = { it.reasonName },
                        selectedIds = selectedImmunoReason.value
                    ) { ids, labels ->

                        selectedImmunoReason.value = ids

                        selectedImmunoReasonJsonArray = JsonArray(emptyList())

                        ids.forEachIndexed { index, id ->
                            val obj = buildJsonObject {
                                put("id", id)
                                put("label", labels.getOrNull(index) ?: "none")
                            }
                            selectedImmunoReasonJsonArray = JsonArray(
                                selectedImmunoReasonJsonArray + obj
                            )
                        }

                        val jsonString = selectedImmunoReasonJsonArray.toString()
                        println("Immunocompromised JSON: $jsonString")
                    }

                }

                SearchableSpinnerDialog(
                    label = "Signs",
                    items = signsReasons,
                    itemId = { it.signId },
                    itemLabel = { it.signName },
                    selectedId = selectedSign.first
                ) { id, label ->
                    selectedSign = id to label

                    selectedSignObj = buildJsonObject {
                        put("id", id)
                        put("label", label ?: "none")
                    }

                    val jsonString = selectedSignObj.toString()
                    println("Signs JSON: $jsonString")
                }


                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = indwellingCompromised, onCheckedChange = { indwellingCompromised = it })
                    Spacer(Modifier.width(8.dp))
                    Text("Indwelling Devices")
                }

                if (indwellingCompromised) {
                    MultiSelectSearchableSpinnerDialog(
                        label = "Indwelling",
                        items = indwellingReasons,
                        itemId = { it.indwellingId },
                        itemLabel = { it.indwellingName },
                        selectedIds = selectedIndwelling.value
                    ) { ids, labels ->

                        selectedIndwelling.value = ids

                        selectedIndwellingJsonArray = JsonArray(emptyList())

                        ids.forEachIndexed { index, id ->
                            val obj = buildJsonObject {
                                put("id", id)
                                put("label", labels.getOrNull(index) ?: "none")
                            }
                            selectedIndwellingJsonArray = JsonArray(
                                selectedIndwellingJsonArray + obj
                            )
                        }

                        val jsonString = selectedIndwellingJsonArray.toString()
                        println("Indwelling JSON: $jsonString")
                    }

                }



                Divider()
                Text("Vital Signs", style = MaterialTheme.typography.titleMedium)

                patientTextField(hr, "Heart Rate (HR)", true) { hr = it }
// Blood Pressure - two fields side by side
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    patientTextField(
                        value = bpSystolic,
                        label = "Systolic",
                        isNumber = true,
                        modifier = Modifier.weight(1f) // so both share space equally
                    ) { bpSystolic = it }

                    patientTextField(
                        value = bpDiastolic,
                        label = "Diastolic",
                        isNumber = true,
                        modifier = Modifier.weight(1f) // so both share space equally
                    ) { bpDiastolic = it }
                }

                patientTextField(temp, "Temperature (°F)") { temp = it }
                patientTextField(rr, "Respiratory Rate (RR)", true) { rr = it }

                MultiSelectSearchableSpinnerDialog(
                    label = "Diagnosis",
                    items = diseasesList,
                    itemId = { it.diseaseID },
                    itemLabel = { it.diseaseName },
                    selectedIds = selecteddiseasesList.value
                ) { ids, labels ->

                    selecteddiseasesList.value = ids

                    selectedDiseasesListJsonArray = JsonArray(emptyList())

                    ids.forEachIndexed { index, id ->
                        val obj = buildJsonObject{
                            put("id", id)
                            put("label", labels.getOrNull(index) ?: "none")
                        }
                        selectedDiseasesListJsonArray = JsonArray(
                            selectedDiseasesListJsonArray + obj
                        )
                    }

                    val jsonString = selectedDiseasesListJsonArray.toString()
                    println("selecteddiseasesList JSON: $jsonString")
                }
                Spacer(modifier = Modifier.height(20.dp))
                GradientButton(
                    text = "Submit",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onClick = {
                        println(selectedSymptomsJsonArray)
                        viewModel.submitPatientInfo(
                            "none",
                            "1",
                            age,
                            weight,
                            selectedSymptomsJsonArray.toString(),
                            recentHospitalStay.toString(),
                            selectedImmunoReasonJsonArray.toString(),
                            selectedSignObj.toString(),
                            selectedIndwellingJsonArray.toString(),
                            hr,
                            bpSystolic,
                            bpDiastolic,
                            temp,
                            rr,
                            selectedDiseasesListJsonArray.toString()
                        )
                        onSubmit()
                    }
                )

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

}




@Preview(showBackground = true)
@Composable
fun PreviewPatientInfoInputScreen() {
//    PatientInfoInputScreen { age, weight, recentStay, immune, reason, comorbid, hr, bp, temp, rr ->
//        println("Age: $age, Weight: $weight")
//        println("Recent Hospital: $recentStay, Immunocompromised: $immune, Reason: $reason")
//        println("Comorbidities: $comorbid")
//        println("HR: $hr, BP: $bp, Temp: $temp, RR: $rr")
//    }
}

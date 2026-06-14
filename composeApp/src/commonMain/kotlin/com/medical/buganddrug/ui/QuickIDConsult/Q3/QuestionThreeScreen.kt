package com.medical.buganddrug.ui.QuickIDConsult.Q3


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll



import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import buganddrug_multiplateform.composeapp.generated.resources.Res
import buganddrug_multiplateform.composeapp.generated.resources.arrow_drop_down
import buganddrug_multiplateform.composeapp.generated.resources.first_aid_kit
import buganddrug_multiplateform.composeapp.generated.resources.info
import buganddrug_multiplateform.composeapp.generated.resources.reportissue

import com.medical.buganddrug.ui.QuickIDConsult.topBar
import com.medical.buganddrug.ui.theme.cardLightBackgroundColor
import com.medical.buganddrug.ui.theme.cardSoftBackgroundColor
import com.medical.buganddrug.util.ErrorAlertDialog
import com.medical.buganddrug.util.LoadingOverlay
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

@Composable
fun QuestionThreeScreen(
    viewModel: QuestionThreeViewModel,
    onSubmit: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) {
        viewModel.getQ3Data()
    viewModel.getAntibiotic()
    }

    val url = viewModel.getSyndromeIdentificationData?.url
    val ivToPOs = viewModel.getSyndromeIdentificationData?.iVtoPOs ?: emptyList()
    val isLoading by viewModel.loading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

//    val context = LocalContext.current

    // State for Part 1: Selective Questions
    var currentStep by remember { mutableStateOf(1) }
    var isOnIVAntimicrobials by remember { mutableStateOf<String?>(null) }
    var antimicrobialName by remember { mutableStateOf("") }
    var isClinicallyStable by remember { mutableStateOf<String?>(null) }
    var isToleratingOral by remember { mutableStateOf<String?>(null) }
    var hasEffectivePO by remember { mutableStateOf<String?>(null) }
    var isSourceControlled by remember { mutableStateOf<String?>(null) }
    var isImmunocompetent by remember { mutableStateOf<String?>(null) }
    var pdfLoading by remember { mutableStateOf(true) }

//    val webView = remember {
//        WebView(context).apply {
//            settings.javaScriptEnabled = true
//        }
//    }
//    LaunchedEffect(url) {
//        url?.let {
//            val baseUrl = "https://bder.duhs.edu.pk"
//            webView.webViewClient = object : WebViewClient() {
//                override fun onPageFinished(view: WebView?, url: String?) {
//                    pdfLoading = false
//                }
//            }
//            webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$baseUrl$it")
//        }
//    }
    // State for Part 2: PDF Display
    var showPDF by remember { mutableStateOf(false) }

    // State for Part 3: Dropdown
    var showOral by remember { mutableStateOf(false) }
    var selectedConditionId by remember { mutableStateOf<Int?>(null) }

    // Data for Part 3 dropdown
    val conditions = ivToPOs
        .map { it.diseaseId to it.diseaseName }
        .distinctBy { it.first }
        .map { it.second to it.first }

    // Data for Part 3 table
    data class AntimicrobialOption(
        val iv: String,
        val poEquivalent: String,
        val switchNotes: String
    )

    val antimicrobialOptions = ivToPOs.filter { it.diseaseId == selectedConditionId }


    // Check if all questions in Part 1 are answered "Yes"
    val allQuestionsAnsweredYes = isOnIVAntimicrobials == "Yes" &&
            isClinicallyStable == "Yes" &&
            isToleratingOral == "Yes" &&
            hasEffectivePO == "Yes" &&
            isSourceControlled == "Yes" &&
            isImmunocompetent == "Yes" &&
            antimicrobialName.isNotBlank()

    Scaffold(
        topBar = {
            topBar(
                topic = "Antimicrobial Switch Assessment",
                patientType = "Evaluate IV to PO switch",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFFFFFFF),
                        Color(0xFFF3E5F5)
                    )
                )
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Part 1: Selective Questions
                if (!showPDF && !showOral) {
                    Text(
                        text = "Step $currentStep: IV to PO Antimicrobial Switch",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // Question 1: Patient on IV antimicrobials
                    Column {
                        Text(
                            text = "1. Patient on IV antimicrobials",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isOnIVAntimicrobials == "Yes",
                                onCheckedChange = { checked ->
                                    isOnIVAntimicrobials = if (checked) "Yes" else null
                                    if (checked) {
                                        currentStep = 2
                                    } else {
                                        currentStep = 1
                                        isOnIVAntimicrobials = null
                                    }
                                    // Reset subsequent steps
                                    isClinicallyStable = null
                                    isToleratingOral = null
                                    hasEffectivePO = null
                                    isSourceControlled = null
                                    isImmunocompetent = null
                                }
                            )
                            Text(text = "Yes", modifier = Modifier.padding(start = 4.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Checkbox(
                                checked = isOnIVAntimicrobials == "No",
                                onCheckedChange = { checked ->
                                    isOnIVAntimicrobials = if (checked) "No" else null
                                    if (checked) {
                                        currentStep = 1
                                    }
                                    // Reset subsequent steps
                                    isClinicallyStable = null
                                    isToleratingOral = null
                                    hasEffectivePO = null
                                    isSourceControlled = null
                                    isImmunocompetent = null
                                }
                            )
                            Text(text = "No", modifier = Modifier.padding(start = 4.dp))
                        }
                    }
                    Divider(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        thickness = 0.5.dp
                    )

                    // Antimicrobial Name Input
                    if (isOnIVAntimicrobials == "Yes") {
                        // Antibiotic Dropdown (from API)
                        val antibioticList = viewModel.antibioticDoses
                            .map { it.antibioticName }
                            .distinct()
                        SingleSelectSearchableSpinnerDialog(
                            label = "Select Antimicrobial",
                            items = antibioticList.map { it to it },
                            itemLabel = { it.first },
                            selectedItem = antimicrobialName,
                            onItemSelected = { selected ->
                                antimicrobialName = selected?.second.toString()

                            }


                        )

                        Divider(
                            color = MaterialTheme.colorScheme.outlineVariant,
                            thickness = 0.5.dp
                        )
                    }

                    // Question 2: Clinically Stable
                    if (currentStep >= 2 && isOnIVAntimicrobials == "Yes") {
                        Column {
                            Text(
                                text = "2. Is patient clinically stable?",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isClinicallyStable == "Yes",
                                    onCheckedChange = { checked ->
                                        isClinicallyStable = if (checked) "Yes" else null
                                        if (checked) {
                                            currentStep = 3
                                        } else {
                                            currentStep = 2
                                        }
                                        // Reset subsequent steps
                                        isToleratingOral = null
                                        hasEffectivePO = null
                                        isSourceControlled = null
                                        isImmunocompetent = null
                                    }
                                )
                                Text(text = "Yes", modifier = Modifier.padding(start = 4.dp))
                                Spacer(modifier = Modifier.width(16.dp))
                                Checkbox(
                                    checked = isClinicallyStable == "No",
                                    onCheckedChange = { checked ->
                                        isClinicallyStable = if (checked) "No" else null
                                        if (checked) {
                                            currentStep = 2
                                        }
                                        // Reset subsequent steps
                                        isToleratingOral = null
                                        hasEffectivePO = null
                                        isSourceControlled = null
                                        isImmunocompetent = null
                                    }
                                )
                                Text(text = "No", modifier = Modifier.padding(start = 4.dp))
                            }
                            if (isClinicallyStable == "No") {
                                Text(
                                    text = "Continue IV, reassess daily",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                        Divider(
                            color = MaterialTheme.colorScheme.outlineVariant,
                            thickness = 0.5.dp
                        )
                    }

                    // Question 3: Tolerating Oral Intake
                    if (currentStep >= 3 && isClinicallyStable == "Yes") {
                        Column {
                            Text(
                                text = "3. Is patient tolerating oral intake & has functional GI tract?",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isToleratingOral == "Yes",
                                    onCheckedChange = { checked ->
                                        isToleratingOral = if (checked) "Yes" else null
                                        if (checked) {
                                            currentStep = 4
                                        } else {
                                            currentStep = 3
                                        }
                                        // Reset subsequent steps
                                        hasEffectivePO = null
                                        isSourceControlled = null
                                        isImmunocompetent = null
                                    }
                                )
                                Text(text = "Yes", modifier = Modifier.padding(start = 4.dp))
                                Spacer(modifier = Modifier.width(16.dp))
                                Checkbox(
                                    checked = isToleratingOral == "No",
                                    onCheckedChange = { checked ->
                                        isToleratingOral = if (checked) "No" else null
                                        if (checked) {
                                            currentStep = 3
                                        }
                                        // Reset subsequent steps
                                        hasEffectivePO = null
                                        isSourceControlled = null
                                        isImmunocompetent = null
                                    }
                                )
                                Text(text = "No", modifier = Modifier.padding(start = 4.dp))
                            }
                            if (isToleratingOral == "No") {
                                Text(
                                    text = "Continue IV, reassess",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                        Divider(
                            color = MaterialTheme.colorScheme.outlineVariant,
                            thickness = 0.5.dp
                        )
                    }

                    // Question 4: Effective PO Option
                    if (currentStep >= 4 && isToleratingOral == "Yes") {
                        Column {
                            Text(
                                text = "4. Is there an effective PO option based on susceptibility and site penetration?",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = hasEffectivePO == "Yes",
                                    onCheckedChange = { checked ->
                                        hasEffectivePO = if (checked) "Yes" else null
                                        if (checked) {
                                            currentStep = 5
                                        } else {
                                            currentStep = 4
                                        }
                                        // Reset subsequent steps
                                        isSourceControlled = null
                                        isImmunocompetent = null
                                    }
                                )
                                Text(text = "Yes", modifier = Modifier.padding(start = 4.dp))
                                Spacer(modifier = Modifier.width(16.dp))
                                Checkbox(
                                    checked = hasEffectivePO == "No",
                                    onCheckedChange = { checked ->
                                        hasEffectivePO = if (checked) "No" else null
                                        if (checked) {
                                            currentStep = 4
                                        }
                                        // Reset subsequent steps
                                        isSourceControlled = null
                                        isImmunocompetent = null
                                    }
                                )
                                Text(text = "No", modifier = Modifier.padding(start = 4.dp))
                            }
                            if (hasEffectivePO == "No") {
                                Text(
                                    text = "Continue IV, consult ID if prolonged",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                        Divider(
                            color = MaterialTheme.colorScheme.outlineVariant,
                            thickness = 0.5.dp
                        )
                    }

                    // Question 5: Source Controlled
                    if (currentStep >= 5 && hasEffectivePO == "Yes") {
                        Column {
                            Text(
                                text = "5. Is source controlled (abscess drained, devices addressed)?",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isSourceControlled == "Yes",
                                    onCheckedChange = { checked ->
                                        isSourceControlled = if (checked) "Yes" else null
                                        if (checked) {
                                            currentStep = 6
                                        } else {
                                            currentStep = 5
                                        }
                                        // Reset subsequent steps
                                        isImmunocompetent = null
                                    }
                                )
                                Text(text = "Yes", modifier = Modifier.padding(start = 4.dp))
                                Spacer(modifier = Modifier.width(16.dp))
                                Checkbox(
                                    checked = isSourceControlled == "No",
                                    onCheckedChange = { checked ->
                                        isSourceControlled = if (checked) "No" else null
                                        if (checked) {
                                            currentStep = 5
                                        }
                                        // Reset subsequent steps
                                        isImmunocompetent = null
                                    }
                                )
                                Text(text = "No", modifier = Modifier.padding(start = 4.dp))
                            }
                            if (isSourceControlled == "No") {
                                Text(
                                    text = "Address source before switch",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                        Divider(
                            color = MaterialTheme.colorScheme.outlineVariant,
                            thickness = 0.5.dp
                        )
                    }

                    // Question 6: Immunocompetent
                    if (currentStep >= 6 && isSourceControlled == "Yes") {
                        Column {
                            Text(
                                text = "6. Is patient immunocompetent / no special considerations?",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isImmunocompetent == "Yes",
                                    onCheckedChange = { checked ->
                                        isImmunocompetent = if (checked) "Yes" else null
                                        if (checked) {
                                            currentStep = 7
                                        } else {
                                            currentStep = 6
                                        }
                                    }
                                )
                                Text(text = "Yes", modifier = Modifier.padding(start = 4.dp))
                                Spacer(modifier = Modifier.width(16.dp))
                                Checkbox(
                                    checked = isImmunocompetent == "No",
                                    onCheckedChange = { checked ->
                                        isImmunocompetent = if (checked) "No" else null
                                        if (checked) {
                                            currentStep = 6
                                        }
                                    }
                                )
                                Text(text = "No", modifier = Modifier.padding(start = 4.dp))
                            }
                            if (isImmunocompetent == "No") {
                                Text(
                                    text = "Seek ID approval",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                        Divider(
                            color = MaterialTheme.colorScheme.outlineVariant,
                            thickness = 0.5.dp
                        )
                    }

                    // Submit Button for Part 1
                    Button(
                        onClick = {
                            showPDF = false
                            showOral=true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        shape = MaterialTheme.shapes.medium,
                        enabled = allQuestionsAnsweredYes
                    ) {
                        Text("Proceed to Condition Selection")
                    }
                }

                // Part 2: PDF Display
                if (showPDF) {
                    var isLoading by remember { mutableStateOf(true) }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.reportissue),

                                contentDescription = "Report Icon",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Antimicrobial Report",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(500.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                        ) {
//
//                            AndroidView(
//                                factory = { webView },
//                                modifier = Modifier.fillMaxSize(),
//
//                            )

                            if (pdfLoading) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                    // Submit Button for Part 3
                    Button(
                        onClick = {
                            onSubmit()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        shape = MaterialTheme.shapes.medium,
                        enabled = selectedConditionId != null
                    ) {
                        Text("Submit")
                    }

                }


                // Part 3: Condition Dropdown and Table
                if (showOral) {
                    SingleSelectSearchableSpinnerDialog(
                        label = "Select Condition",
                        items = conditions,
                        itemLabel = { it.first },
                        selectedItem = selectedConditionId,
                        onItemSelected = { item ->
                            selectedConditionId = item?.second
                        }
                    )

                    // Display Table
                    if (selectedConditionId != null) {
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
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                // Title
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
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Antimicrobial Options for ${conditions.find { it.second == selectedConditionId }?.first}",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Content list
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(max = 300.dp)
                                ) {
                                    items(antimicrobialOptions) { option ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 6.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(12.dp),
                                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(12.dp)
                                            ) {
                                                Text(
                                                    text = "IV: ${option.iV ?: "-"}",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    buildAnnotatedString {
                                                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                            append("PO Equivalent: ")
                                                        }
                                                        append(option.poEquivalent ?: "-")
                                                    },
                                                    style = MaterialTheme.typography.bodyMedium
                                                )

                                                Spacer(modifier = Modifier.height(4.dp))

                                                Text(
                                                    buildAnnotatedString {
                                                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                            append("PO Equivalent: ")
                                                        }
                                                        append(option.switchNote ?: "-")
                                                    },
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Button to proceed
                    Button(
                        onClick = {
                            showPDF = true
                            showOral=false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        shape = MaterialTheme.shapes.medium,
                        enabled = selectedConditionId!=null

                    ) {
                        Text("Proceed to Pdf")
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
                    Icon(painter = painterResource(Res.drawable.arrow_drop_down), contentDescription = "Select",            modifier = Modifier.size(24.dp) // actual icon size
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = LocalContentColor.current.copy(alpha = 1f),
                disabledLabelColor = LocalContentColor.current.copy(alpha = 1f),
                disabledTrailingIconColor = LocalContentColor.current.copy(alpha = 1f),
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledContainerColor = MaterialTheme.colorScheme.surface
            )
        )
    }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp,
                color = Color.White // 👈 sets Surface background to white

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White) // 👈 ensures Column background is white too
                        .padding(16.dp)
                ) {
                    var searchQuery by remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Search $label") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White), // 👈 text field white background
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF800080),
                            unfocusedBorderColor = Color(0xFFBDBDBD),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White
                        )
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
fun QuestionThreeScreenPreview() {
    val questionViewModel: QuestionThreeViewModel = koinInject()


    MaterialTheme {
        QuestionThreeScreen(
            questionViewModel,
            onSubmit = {},
            onBackClick = {}
        )
    }
}
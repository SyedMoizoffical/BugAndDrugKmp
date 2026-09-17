package com.medical.buganddrug.ui.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import buganddrug_multiplateform.composeapp.generated.resources.Res
import buganddrug_multiplateform.composeapp.generated.resources.app_icon
import buganddrug_multiplateform.composeapp.generated.resources.exit
import buganddrug_multiplateform.composeapp.generated.resources.inpatient
import buganddrug_multiplateform.composeapp.generated.resources.insurance
import buganddrug_multiplateform.composeapp.generated.resources.menu_dots
import buganddrug_multiplateform.composeapp.generated.resources.outpatient
import buganddrug_multiplateform.composeapp.generated.resources.reportissue
import com.medical.buganddrug.data.remote.SharedPreferenceManager
import com.medical.buganddrug.ui.onboarding.loginScreen.AuthViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import com.medical.buganddrug.util.ErrorAlertDialog
import com.medical.buganddrug.util.LoadingOverlay
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.koin.compose.koinInject
import buganddrug_multiplateform.composeapp.generated.resources.search
import com.medical.buganddrug.data.model.LocalStorageDatamodel.DiseaseItem
import com.medical.buganddrug.ui.FilterScreen.ClinicalSyndromeFilterScreen
import com.medical.buganddrug.ui.FilterScreen.EtiologicalAgentFilterScreen
import com.medical.buganddrug.ui.QuickIDConsult.Q5.QuestionSixFilterScreen
import com.medical.buganddrug.ui.clinicalSyndrome.ClinicalSyndromeViewModel
import com.medical.buganddrug.ui.EtiologicalAgentScreen.EtiologicalAgentScreenViewModel
import com.medical.buganddrug.ui.QuickIDConsult.Q5.QuestionSixViewModel
import org.koin.compose.koinInject

import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientTypeSelectionScreen(
    onPatientInfoClick: () -> Unit = {},
    onPostSurveyClick: () -> Unit = {},
    onPreSurveyClick: () -> Unit = {},
    onInPatientTypeClick: () -> Unit = {},
    onOutPatientTypeClick: () -> Unit = {},
    onBugReportClick: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    authViewModel: AuthViewModel,
    sharedPrefs: SharedPreferenceManager = koinInject()
) {
    val isLoading by authViewModel.loading.collectAsState()
    val errorMessage by authViewModel.errorMessage.collectAsState()
    val diseaseList by authViewModel.diseaseList.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var selectedDiseaseForDialog by remember { mutableStateOf<DiseaseItem?>(null) }

    var showDisclaimer by remember { mutableStateOf(sharedPrefs.getDisclaimer() != "true") }
    var selectedPatientType by remember { mutableStateOf<String?>(null) }
    var showError by remember { mutableStateOf(false) }

//    val context = LocalContext.current
//    val sharedPrefs = remember { SharedPreferenceManager(context) }
//    val resultString = sharedPrefs.getPatientData()?.toString() ?: ""
//    val getDisclaimer = sharedPrefs.getDisclaimer()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        authViewModel.printExtractedDiseaseList()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(320.dp),
                drawerShape = RoundedCornerShape(topEnd = 36.dp, bottomEnd = 36.dp),
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerContentColor = MaterialTheme.colorScheme.onSurface,
            ) {
                DrawerHeaderElegant()

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 28.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(16.dp))

                val items = listOf(
                    Triple("Bug Report", painterResource(Res.drawable.reportissue),
                        onBugReportClick),
                    Triple("Privacy Policy", painterResource(Res.drawable.insurance), onPrivacyPolicyClick),
                    Triple("Sign Out", painterResource(Res.drawable.exit), onLogoutClick),

                )

                items.forEach { (title, icon, action) ->
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                painter = icon,
                                contentDescription = title,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            action()
                        },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.Transparent,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        shape = RoundedCornerShape(28.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Bug & Drug",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF800080) // Using your theme purple
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    val appVersion = 1.0
                    Text(
                        text = "Version $appVersion • 2026",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                        thickness = 1.dp
                    )
                    CopyrightFooter(
                        isCompact = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                // Your exact original Top Bar
                patientTopBar(
                    topic = "Bug & Drug",
                    patientType = "Infectious Diseases Clinical Guide",
                    onBackClick = { selectedPatientType = null },
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
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
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                // Uncomment when using your real dialog
                /*
                if (showError) {
                    ErrorAlertDialog(
                        errorMessage = "Please enter Patient info first",
                        onDismiss = { showError = false }
                    )
                }
                */

                AnimatedContent(targetState = selectedPatientType, label = "patient_anim") { type ->
                    if (type == null) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top,
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .imePadding()
                        ) {
                            // Search Bar (Placed on top of Logo Image)
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = {
                                    Text(
                                        text = "Search Disease, Organism, Antibiotic...",
                                        fontSize = 12.sp,
                                        color = Color(0xFF94A3B8),
                                        maxLines = 1
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
                                    if (searchQuery.isNotEmpty() || isSearchActive) {
                                        IconButton(onClick = {
                                            searchQuery = ""
                                            isSearchActive = false
                                        }) {
                                            Text("✕", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onFocusChanged { if (it.isFocused) isSearchActive = true }
                                    .padding(bottom = 12.dp),
                                shape = RoundedCornerShape(18.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedBorderColor = Color(0xFF800080),
                                    unfocusedBorderColor = Color(0xFFE2E8F0)
                                ),
                                singleLine = true
                            )

                            // Alphabetically Sorted Search Results Dropdown List
                            val filteredDiseases = remember(searchQuery, diseaseList) {
                                val baseList = if (searchQuery.isBlank()) {
                                    diseaseList
                                } else {
                                    diseaseList.filter { it.name.contains(searchQuery, ignoreCase = true) }
                                }
                                baseList.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name })
                            }

                            if (isSearchActive || searchQuery.isNotBlank()) {
                                ElevatedCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(max = 280.dp)
                                        .padding(bottom = 16.dp),
                                    shape = RoundedCornerShape(18.dp),
                                    elevation = CardDefaults.elevatedCardElevation(4.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White)
                                ) {
                                    if (filteredDiseases.isEmpty()) {
                                        Text(
                                            text = "No matching records found",
                                            modifier = Modifier.padding(16.dp),
                                            fontSize = 14.sp,
                                            color = Color(0xFF64748B)
                                        )
                                    } else {
                                        LazyColumn(
                                            modifier = Modifier.fillMaxWidth(),
                                            contentPadding = PaddingValues(vertical = 4.dp)
                                        ) {
                                            items(
                                                items = filteredDiseases,
                                                key = { "${it.type}_${it.id}_${it.name}" }
                                            ) { disease ->
                                                Column {
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .clickable {
                                                                searchQuery = ""
                                                                isSearchActive = false
                                                                selectedDiseaseForDialog = disease
                                                            }
                                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.SpaceBetween
                                                    ) {
                                                        Text(
                                                            text = disease.name,
                                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                                fontWeight = FontWeight.Bold,
                                                                color = Color(0xFF1E293B)
                                                            ),
                                                            modifier = Modifier.weight(1f)
                                                        )
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        val tagBg = when (disease.type.lowercase()) {
                                                            "disease" -> Color(0xFFF3E5F5)
                                                            "organism", "organisum" -> Color(0xFFE0F2FE)
                                                            "antibiotic" -> Color(0xFFDCFCE7)
                                                            else -> Color(0xFFF1F5F9)
                                                        }
                                                        val tagColor = when (disease.type.lowercase()) {
                                                            "disease" -> Color(0xFF800080)
                                                            "organism", "organisum" -> Color(0xFF0369A1)
                                                            "antibiotic" -> Color(0xFF15803D)
                                                            else -> Color(0xFF475569)
                                                        }
                                                        Surface(
                                                            shape = RoundedCornerShape(8.dp),
                                                            color = tagBg
                                                        ) {
                                                            Text(
                                                                text = disease.type.uppercase(),
                                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                                fontSize = 11.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = tagColor
                                                            )
                                                        }
                                                    }
                                                    HorizontalDivider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(horizontal = 16.dp))
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            TitleLogo(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 14.dp),
                                small = false
                            )

                            Text(
                                text = "Select Patient Type",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF800080)
                                ),
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                PatientTypeCard(
                                    icon = painterResource(Res.drawable.inpatient),
                                    title = "In Patients",
                                    isSelected = false,
                                    onClick = {
                                        onInPatientTypeClick()
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                                PatientTypeCard(
                                    icon = painterResource(Res.drawable.outpatient),
                                    title = "Out Patients",
                                    isSelected = false,
                                    onClick = {
                                        onOutPatientTypeClick()
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(28.dp))

                            // Using your GradientButton
                            GradientButton(
                                text = "View Patient Info",
                                modifier = Modifier.fillMaxWidth().height(52.dp),
                                onClick = { onPatientInfoClick() }
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Grouped Survey Buttons Side-by-Side for a cleaner look
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                GradientButton(
                                    text = "Pre Survey",
                                    modifier = Modifier.weight(1f).height(48.dp),
                                    onClick = { onPreSurveyClick() }
                                )
                                GradientButton(
                                    text = "Post Survey",
                                    modifier = Modifier.weight(1f).height(48.dp),
                                    onClick = { onPostSurveyClick() }
                                )
                            }
                        }
                    }
                }
            }

            // Uncomment when using your real dialog

            if (showDisclaimer) {
                DisclaimerDialog(onDismiss = {
                    showDisclaimer = false
                    sharedPrefs.saveDisclaimer("true")
                })
            }
            if (showError) {
                ErrorAlertDialog(
                    errorMessage = "Fill patient info first. Tap 'Patient Info'.",
                    onDismiss = { showError =false }
                )
            }
            if (isLoading) {
                LoadingOverlay()
            }
            if (errorMessage != null) {
                ErrorAlertDialog(
                    errorMessage = errorMessage,
                    onDismiss = { authViewModel.clearError() }
                )
            }

            selectedDiseaseForDialog?.let { disease ->
                var showContent by remember(disease) { mutableStateOf(true) }

                LaunchedEffect(showContent) {
                    if (!showContent) {
                        kotlinx.coroutines.delay(220)
                        selectedDiseaseForDialog = null
                    }
                }

                val animateDismiss = {
                    showContent = false
                }

                Dialog(
                    onDismissRequest = animateDismiss,
                    properties = DialogProperties(
                        usePlatformDefaultWidth = false,
                        decorFitsSystemWindows = false,

                        dismissOnBackPress = true,
                        dismissOnClickOutside = false
                    )
                ) {
                    AnimatedVisibility(
                        visible = showContent,
                        enter = slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing)
                        ) + fadeIn(animationSpec = tween(durationMillis = 220)),
                        exit = slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing)
                        ) + fadeOut(animationSpec = tween(durationMillis = 220))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background)
                        ) {
                            when {
                                disease.type.equals("disease", ignoreCase = true) -> {
                                    val vm: ClinicalSyndromeViewModel = koinInject()
                                    ClinicalSyndromeFilterScreen(
                                        viewModel = vm,
                                        onBackClick = animateDismiss,
                                        diseaseId = disease.id,
                                        diseaseName = disease.name
                                    )
                                }
                                disease.type.equals("organism", ignoreCase = true) || disease.type.equals("organisum", ignoreCase = true) -> {
                                    val vm: EtiologicalAgentScreenViewModel = koinInject()
                                    EtiologicalAgentFilterScreen(
                                        viewModel = vm,
                                        onBackClick = animateDismiss,
                                        organismId = disease.id,
                                        organismName = disease.name
                                    )
                                }
                                disease.type.equals("antibiotic", ignoreCase = true) -> {
                                    val vm: QuestionSixViewModel = koinInject()
                                    QuestionSixFilterScreen(
                                        viewModel = vm,
                                        onBackClick = animateDismiss,
                                        antibioticId = disease.id,
                                        antibioticName = disease.name
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

@Composable
fun PatientTypeCard(
    icon: Painter,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        if (isSelected) Color(0xFF800080) else Color.White, label = "bg_color"
    )
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.03f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow), label = "scale_anim"
    )

    Card(
        modifier = modifier
            .height(140.dp)
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp
        ),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = if (!isSelected) BorderStroke(1.dp, Color(0xFFE2E8F0)) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon placed inside a soft circular background
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(
                        color = if (isSelected) Color.White.copy(alpha = 0.15f) else Color(0xFFF3E5F5),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = icon,
                    contentDescription = title,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                color = if (isSelected) Color.White else Color(0xFF1B2B5D)
            )
        }
    }
}

@Composable
fun TitleLogo(modifier: Modifier = Modifier, small: Boolean = false) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(Res.drawable.app_icon),
            contentDescription = "App Logo",
            modifier = if (small)
                Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(24.dp))
            else
                Modifier
                    .size(140.dp)
                    .clip(RoundedCornerShape(32.dp))
        )
        Text(
            text = "Clinical Decision Support App",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color(0xFF64748B),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                fontSize = 13.sp,
                letterSpacing = 0.3.sp
            ),
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}

// Exactly as you provided it
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun patientTopBar(
    topic: String,
    patientType: String,
    onBackClick: () -> Unit = {},     // optional – keep or remove
    onMenuClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()           // ← key: fill the available height
                    .wrapContentHeight(align = Alignment.CenterVertically), // center vertically
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center               // centers children vertically
            ) {
                Text(
                    text = topic,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
                Text(
                    text = patientType,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        },

        navigationIcon = {
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .size(width = 40.dp, height = 40.dp)
                        .clip(RoundedCornerShape(12.dp)) // card radius
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                        .clickable { onMenuClick() },   // click here instead of IconButton
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painterResource(Res.drawable.menu_dots),
                        contentDescription = "Menu",
                        tint = Color(0xFF0F172A),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        // Optional right-side back icon if needed later
        // actions = {
        //     IconButton(onClick = onBackClick) {
        //         Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
        //     }
        // },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            scrolledContainerColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)  // your custom tall height
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF6A1B9A),
                        Color(0xFFCE93D8)
                    )
                ),
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
            )
    )
}

@Composable
private fun DrawerHeaderElegant() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF3E5F5), // Soft lavender top
                        Color.Transparent
                    )
                )
            )
            .padding(top = 48.dp, bottom = 32.dp, start = 28.dp, end = 28.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier.size(64.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.app_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))   // corner radius
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Bug & Drug",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Color(0xFF1B2B5D) // Your dark blue
        )

        Text(
            text = "Infectious Diseases Clinical Guide",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF4B5563)
        )
    }
}
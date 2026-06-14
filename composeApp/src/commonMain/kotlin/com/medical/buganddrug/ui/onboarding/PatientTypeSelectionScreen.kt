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
import com.medical.buganddrug.util.ErrorAlertDialog

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
    onBackClick: () -> Unit = {}
) {
    var showDisclaimer by remember { mutableStateOf(false) }
    var selectedPatientType by remember { mutableStateOf<String?>(null) }
    var showError by remember { mutableStateOf(false) }

//    val context = LocalContext.current
//    val sharedPrefs = remember { SharedPreferenceManager(context) }
//    val resultString = sharedPrefs.getPatientData()?.toString() ?: ""
//    val getDisclaimer = sharedPrefs.getDisclaimer()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

//    LaunchedEffect(getDisclaimer) {
//        if (getDisclaimer.isNullOrEmpty()) {
//            showDisclaimer = true
//        }
//    }

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

                Divider(
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
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Bug & Drug",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF800080) // Using your theme purple
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val appVersion = 1.0
                    Text(
                        text = "Version $appVersion • 2026",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "© 2026 AI/ML Clinical Guide",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
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
                    patientType = "AI/ML Clinical Guide",
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
                            modifier = Modifier.fillMaxSize()
                        ) {
                            TitleLogo(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 24.dp),
                                small = false
                            )

                            Text(
                                text = "Select Patient Type",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF800080) // Your purple
                                ),
                                modifier = Modifier.padding(bottom = 24.dp)
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
//                                        if(resultString.isEmpty()){
//                                            showError = true
//                                        } else {
                                            onInPatientTypeClick()
                                       // }
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                                PatientTypeCard(
                                    icon = painterResource(Res.drawable.outpatient),
                                    title = "Out Patients",
                                    isSelected = false,
                                    onClick = {
//                                        if(resultString.isEmpty()){
//                                            showError = true
//                                        } else {
                                            onOutPatientTypeClick()
                                        //}
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(40.dp))

                            // Using your GradientButton
                            GradientButton(
                                text = "View Patient Info",
                                modifier = Modifier.fillMaxWidth().height(54.dp),
                                onClick = { onPatientInfoClick() }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Grouped Survey Buttons Side-by-Side for a cleaner look
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                GradientButton(
                                    text = "Pre Survey",
                                    modifier = Modifier.weight(1f).height(50.dp),
                                    onClick = { onPreSurveyClick() }
                                )
                                GradientButton(
                                    text = "Post Survey",
                                    modifier = Modifier.weight(1f).height(50.dp),
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
                   // sharedPrefs.saveDisclaimer("true")
                })
            }
            if (showError) {
                ErrorAlertDialog(
                    errorMessage = "Fill patient info first. Tap 'Patient Info'.",
                    onDismiss = { showError =false }
                )
            }

        }
    }
}

@Composable
fun PatientTypeCard(
    icon:Painter,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        if (isSelected) Color(0xFF800080) else Color.White, label = "bg_color"
    )
    val contentColor by animateColorAsState(
        if (isSelected) Color.White else Color(0xFF800080), label = "content_color"
    )
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.03f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow), label = "scale_anim"
    )

    Card(
        modifier = modifier
            .height(150.dp)
            .scale(scale)
            .shadow(
                elevation = if (isSelected) 12.dp else 4.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = if (isSelected) Color(0xFF800080) else Color.Black.copy(alpha = 0.1f)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        // Add a very subtle border when unselected to make it look crisp against the white/lavender background
        border = if (!isSelected) BorderStroke(1.dp, Color(0xFFE5E7EB)) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon placed inside a soft circular background
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = if (isSelected) Color.White.copy(alpha = 0.15f) else Color(0xFFF3E5F5),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = icon,
                    contentDescription = title,
                    tint = Color.Unspecified, // keep original colors

                    //  tint = contentColor,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
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
                    .size(110.dp)
                    .clip(RoundedCornerShape(32.dp))
            else
                Modifier
                    .size(180.dp) // Slightly scaled down to prevent crowding
                    .clip(RoundedCornerShape(40.dp))
        )
        Text(
            text = "AI/ML Clinical Decision Support",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color(0xFF4B5563),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(top = 12.dp)
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
            text = "AI/ML Clinical Guide",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF4B5563)
        )
    }
}
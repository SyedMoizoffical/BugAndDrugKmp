package com.medical.buganddrug.ui.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import buganddrug_multiplateform.composeapp.generated.resources.Res
import buganddrug_multiplateform.composeapp.generated.resources.app_icon
import buganddrug_multiplateform.composeapp.generated.resources.info
import com.medical.buganddrug.data.remote.SharedPreferenceManager
import com.medical.buganddrug.ui.onboarding.loginScreen.AuthViewModel
import com.medical.buganddrug.util.ErrorAlertDialog
import com.medical.buganddrug.util.LoadingOverlay
import org.jetbrains.compose.resources.painterResource


@Composable
fun Title(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(Res.drawable.app_icon),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(140.dp)
                .padding(bottom = 12.dp)
        )

        Text(
            text = "Welcome to Bug & Drug 🧫",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Begin your medical assessment in just a few taps",
            fontSize = 16.sp,
            lineHeight = 22.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

@Composable
fun WelcomeScreen(

    authViewModel: AuthViewModel,
    onNavigateToSignIn: () -> Unit = {},

) {
    val data by authViewModel.uiState.collectAsState()



    LaunchedEffect(data.isExistingUser) {
        if (data.isExistingUser == true) {
            onNavigateToSignIn()
            authViewModel.clearNavigation()
        }
    }
    //val context = LocalContext.current
   // val sharedPrefs = remember { SharedPreferenceManager(context) }
 //   val resultString = sharedPrefs.getEmail()

//    LaunchedEffect(resultString) {
//        if (!resultString.isNullOrEmpty()) {
//            authViewModel.signInWithEmail(resultString)
//        }
//    }

    if (data.error != null) {
        ErrorAlertDialog(errorMessage = data.error, onDismiss = { authViewModel.clearError() })
    }
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFFFFF),
            Color(0xFFF3E5F5)   // Very soft lavender
        )
    )




    var isSignUp by remember { mutableStateOf(false) }

    // Form fields
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(if (isSignUp) "" else "") }
    var password by remember { mutableStateOf("") }
    var pmdc by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            },
        contentAlignment = Alignment.Center
    ) {
        // Delay for card appearance
        var showCard by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(500)
            showCard = true
        }

        val alpha by animateFloatAsState(
            targetValue = if (showCard) 1f else 0f,
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            label = "cardFadeIn"
        )

        if (showCard || alpha > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight()
                    .padding(4.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFFD9B9FF), Color(0xFFE0C3FC))
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .alpha(alpha),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                    shape = RoundedCornerShape(22.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 36.dp, horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Image(
                            painter = painterResource(Res.drawable.app_icon),
                            contentDescription = "App Logo",
                            modifier =
                                Modifier.size(120.dp)
                                    .clip(RoundedCornerShape(40.dp))


                        )

                        Text(
                            text = "Welcome to Bug & Drug 🧫",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (isSignUp) "Create your account" else "Sign in to continue",
                            fontSize = 16.sp,
                            color = Color.Gray.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Sign Up Fields (visible only in sign-up mode)
                        AnimatedVisibility(visible = isSignUp) {
                            Column {
                                OutlinedTextField(
                                    value = name,
                                    onValueChange = { name = it },
                                    label = { Text("Name") },
                                    singleLine = true,
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }

                        // Email Field (always visible)
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            ),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Password & PMDC (only in sign-up)
                        AnimatedVisibility(visible = isSignUp) {
                            Column {
                                OutlinedTextField(
                                    value = password,
                                    onValueChange = { password = it },
                                    label = { Text("Password") },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Password,
                                        imeAction = ImeAction.Next
                                    ),
                                    singleLine = true,
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                OutlinedTextField(
                                    value = pmdc,
                                    onValueChange = { pmdc = it },
                                    label = { Text("PMDC") },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                                    singleLine = true,
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(28.dp))
                            }
                        }

                        if (!isSignUp) {
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // Action Button
                        GradientButton(
                            text = if (isSignUp) "Sign Up" else "Sign In",
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                focusManager.clearFocus()
                                if (isSignUp) {
                                   // onNavigateToSignIn()

                                    authViewModel.signUp(name, email, password, pmdc)
                                } else {
                                 //   onNavigateToSignIn()
                                    authViewModel.signInWithEmail(email)
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Toggle between Sign In / Sign Up
                        Row {
                            Text(
                                text = if (isSignUp) "Already have an account? " else "Don't have an account? ",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                            Text(
                                text = if (isSignUp) "Sign In" else "Sign Up",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.clickable {
                                    isSignUp = !isSignUp

                                }
                            )
                        }
                    }
                }
            }
        }
        if (data.isLoading) {
            LoadingOverlay()
        }
    }
}

@Composable
fun GradientButton(
    text: String,
    modifier: Modifier = Modifier,
    gradient: Brush = Brush.verticalGradient(
        listOf(
            Color(0xFF800080),
            Color(0xFFCE93D8)
        )    ),
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(gradient, shape = RoundedCornerShape(16.dp))
            .height(54.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}



// 🪄 Extension to support gradient button color
@Composable
fun Brush.toBrushColor(): Color {
    return remember(this) { Color.Transparent }
}



@Preview(showBackground = true)
@Composable
fun PreviewWelcomeScreen() {
    //WelcomeScreen({},{},{})
}

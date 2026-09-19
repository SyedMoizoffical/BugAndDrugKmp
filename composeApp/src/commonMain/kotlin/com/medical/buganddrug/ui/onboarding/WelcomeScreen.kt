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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import buganddrug_multiplateform.composeapp.generated.resources.Res
import buganddrug_multiplateform.composeapp.generated.resources.app_icon
import buganddrug_multiplateform.composeapp.generated.resources.info
import com.medical.buganddrug.data.remote.SharedPreferenceManager
import com.medical.buganddrug.ui.onboarding.loginScreen.AuthViewModel
import com.medical.buganddrug.util.ErrorAlertDialog
import com.medical.buganddrug.util.SuccessAlertDialog
import com.medical.buganddrug.util.LoadingOverlay
import org.jetbrains.compose.resources.painterResource


import androidx.compose.foundation.border
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

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

private fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    return emailRegex.matches(email.trim())
}

private fun isValidPassword(password: String): Boolean {
    if (password.length < 6) return false
    val hasUpper = password.any { it.isUpperCase() }
    val hasLower = password.any { it.isLowerCase() }
    val hasSpecialOrDigit = password.any { it.isDigit() || !it.isLetterOrDigit() }
    return hasUpper && hasLower && hasSpecialOrDigit
}

@Composable
fun WelcomeScreen(
    authViewModel: AuthViewModel,
    onNavigateToSignIn: () -> Unit = {}
) {
    val data by authViewModel.uiState.collectAsState()

    // Check if local database already has data on first launch
    LaunchedEffect(Unit) {
        authViewModel.checkLocalDataExists()
    }

    // If local data exists, skip welcome and go directly to sign-in
    LaunchedEffect(data.hasLocalData) {
        if (data.hasLocalData) {
            onNavigateToSignIn()
            return@LaunchedEffect
        }
    }

    LaunchedEffect(data.isExistingUser) {
        if (data.isExistingUser == true) {
            onNavigateToSignIn()
            authViewModel.clearNavigation()
        }
    }

    if (data.error != null) {
        ErrorAlertDialog(errorMessage = data.error, onDismiss = { authViewModel.clearError() })
    }

    if (data.successMessage != null) {
        SuccessAlertDialog(errorMessage = data.successMessage, onDismiss = { authViewModel.clearSuccessMessage() })
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
    var email by remember { mutableStateOf(authViewModel.getSavedEmail() ?: "") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    // 5-minute (300s) OTP countdown timer
    var remainingSeconds by remember { mutableStateOf(300) }

    LaunchedEffect(data.isOtpSent) {
        if (data.isOtpSent) {
            remainingSeconds = 300
            while (remainingSeconds > 0) {
                kotlinx.coroutines.delay(1000)
                remainingSeconds--
            }
        }
    }

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val formattedTime = "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"

    val isEmailValid = remember(email) { isValidEmail(email) }
    val isPasswordValid = remember(password) { isValidPassword(password) }
    val isConfirmPasswordValid = remember(password, confirmPassword) {
        confirmPassword.isNotBlank() && confirmPassword == password
    }

    val isFormValid = if (isSignUp) {
        name.isNotBlank() && isEmailValid && isPasswordValid && isConfirmPasswordValid
    } else {
        isEmailValid && password.isNotBlank()
    }

    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(12.dp))

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
                                modifier = Modifier
                                    .size(120.dp)
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

                            if (data.isOtpSent) {
                                // ---------------- OTP VERIFICATION VIEW ----------------
                                Text(
                                    text = "Verify Your Email",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Message on top: Your OTP will expire in 5 min
                                Surface(
                                    color = if (remainingSeconds > 0) Color(0xFFFFF3E0) else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = if (remainingSeconds > 0) {
                                                "⏱️ Your OTP will expire in 5 min ($formattedTime)"
                                            } else {
                                                "⚠️ OTP expired. Please request a new code."
                                            },
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (remainingSeconds > 0) Color(0xFFE65100) else MaterialTheme.colorScheme.error,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "We sent a 6-digit verification code to\n${data.otpEmail}",
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 20.sp
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                OtpInputField(
                                    otpText = otp,
                                    otpCount = 6,
                                    onOtpTextChange = { otp = it }
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                GradientButton(
                                    text = "Verify Email",
                                    enabled = otp.length == 6,
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        focusManager.clearFocus()
                                        authViewModel.verifyEmail(otp.trim())
                                    }
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Resend OTP Row
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Didn't receive code? ",
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "Resend OTP",
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.clickable {
                                            otp = ""
                                            remainingSeconds = 300
                                            authViewModel.resendOtp()
                                        }
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "Change email or back to sign up",
                                    color = Color.Gray,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.clickable {
                                        otp = ""
                                        authViewModel.resetOtpState()
                                    }
                                )

                            } else {
                                // ---------------- SIGN IN / SIGN UP FORM ----------------
                                Text(
                                    text = if (isSignUp) "Create your account" else "Sign in to continue",
                                    fontSize = 16.sp,
                                    color = Color.Gray.copy(alpha = 0.8f),
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(32.dp))

                                // Name Field (visible only in sign-up mode)
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
                                    isError = email.isNotBlank() && !isEmailValid,
                                    supportingText = {
                                        if (email.isNotBlank() && !isEmailValid) {
                                            Text(
                                                text = "Please enter a valid email address",
                                                color = MaterialTheme.colorScheme.error,
                                                fontSize = 12.sp
                                            )
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Email,
                                        imeAction = ImeAction.Next
                                    ),
                                    singleLine = true,
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Password Field (always visible for both Sign In and Sign Up)
                                OutlinedTextField(
                                    value = password,
                                    onValueChange = { password = it },
                                    label = { Text("Password") },
                                    isError = isSignUp && password.isNotBlank() && !isPasswordValid,
                                    supportingText = {
                                        if (isSignUp && password.isNotBlank() && !isPasswordValid) {
                                            Text(
                                                text = "Min 6 characters, uppercase, lowercase & special char/digit",
                                                color = MaterialTheme.colorScheme.error,
                                                fontSize = 12.sp
                                            )
                                        }
                                    },
                                    trailingIcon = {
                                        TextButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                            Text(
                                                text = if (isPasswordVisible) "Hide" else "Show",
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    },
                                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Password,
                                        imeAction = if (isSignUp) ImeAction.Next else ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(onDone = {
                                        focusManager.clearFocus()
                                        if (!isSignUp && isFormValid) {
                                            authViewModel.signIn(email.trim(), password)
                                        }
                                    }),
                                    singleLine = true,
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Confirm Password (only in sign-up)
                                AnimatedVisibility(visible = isSignUp) {
                                    Column {
                                        OutlinedTextField(
                                            value = confirmPassword,
                                            onValueChange = { confirmPassword = it },
                                            label = { Text("Confirm Password") },
                                            isError = confirmPassword.isNotBlank() && !isConfirmPasswordValid,
                                            supportingText = {
                                                if (confirmPassword.isNotBlank() && !isConfirmPasswordValid) {
                                                    Text(
                                                        text = "Passwords do not match",
                                                        color = MaterialTheme.colorScheme.error,
                                                        fontSize = 12.sp
                                                    )
                                                }
                                            },
                                            trailingIcon = {
                                                TextButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                                                    Text(
                                                        text = if (isConfirmPasswordVisible) "Hide" else "Show",
                                                        fontSize = 12.sp,
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                }
                                            },
                                            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                            keyboardOptions = KeyboardOptions(
                                                keyboardType = KeyboardType.Password,
                                                imeAction = ImeAction.Done
                                            ),
                                            keyboardActions = KeyboardActions(onDone = {
                                                focusManager.clearFocus()
                                                if (isFormValid) {
                                                    authViewModel.signUp(
                                                        name = name.trim(),
                                                        email = email.trim(),
                                                        password = password,
                                                        pmdc = "123456789012"
                                                    )
                                                }
                                            }),
                                            singleLine = true,
                                            shape = RoundedCornerShape(14.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }
                                }

                                if (!isSignUp) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                }

                                // Action Button
                                GradientButton(
                                    text = if (isSignUp) "Sign Up" else "Sign In",
                                    enabled = isFormValid,
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        focusManager.clearFocus()
                                        if (isSignUp) {
                                            authViewModel.signUp(
                                                name = name.trim(),
                                                email = email.trim(),
                                                password = password,
                                                pmdc = "123456789012"
                                            )
                                        } else {
                                            authViewModel.signIn(email.trim(), password)
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
            } else {
                Spacer(modifier = Modifier.height(120.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            CopyrightFooter(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        if (data.isLoading) {
            LoadingOverlay()
        }
    }
}

@Composable
fun OtpInputField(
    otpText: String,
    otpCount: Int = 6,
    onOtpTextChange: (String) -> Unit
) {
    BasicTextField(
        value = otpText,
        onValueChange = {
            val filtered = it.filter { char -> char.isDigit() }.take(otpCount)
            onOtpTextChange(filtered)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(otpCount) { index ->
                    val char = if (index < otpText.length) otpText[index].toString() else ""
                    val isFocused = otpText.length == index
                    val isFilled = index < otpText.length

                    Box(
                        modifier = Modifier
                            .width(44.dp)
                            .height(52.dp)
                            .background(
                                color = if (isFocused) Color(0xFFF3E5F5) else Color(0xFFF7F5FA),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(
                                width = if (isFocused) 2.dp else if (isFilled) 1.5.dp else 1.dp,
                                color = when {
                                    isFocused -> MaterialTheme.colorScheme.primary
                                    isFilled -> MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                                    else -> Color(0xFFDCD6E5)
                                },
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun GradientButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    gradient: Brush = Brush.verticalGradient(
        listOf(
            Color(0xFF800080),
            Color(0xFFCE93D8)
        )
    ),
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                if (enabled) gradient else Brush.linearGradient(listOf(Color(0xFFCCCCCC), Color(0xFFD6D6D6))),
                shape = RoundedCornerShape(16.dp)
            )
            .height(54.dp)
            .fillMaxWidth()
            .then(
                if (enabled) Modifier.clickable(onClick = onClick) else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            color = if (enabled) Color.White else Color(0xFF757575)
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

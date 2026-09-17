package com.medical.buganddrug.util

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import com.medical.buganddrug.ui.AppScreens

@Composable
fun ErrorAlertDialog(
    errorMessage: String?,
    onDismiss: () -> Unit,
    onNavigateToSignIn: (() -> Unit)? = null
) {
    if (errorMessage != null) {
        val sanitizedMessage = NetworkErrorHandler.sanitizeMessage(errorMessage)
        val isAuthError = NetworkErrorHandler.isSessionExpired(errorMessage)
        val navigator = LocalNavigator.current

        if (isAuthError) {
            AlertDialog(
                onDismissRequest = { onDismiss() },
                title = { Text("Session Expired") },
                text = { Text("Your session has expired. Please relogin. Would you like to sign in now?") },
                confirmButton = {
                    Button(
                        onClick = {
                            onDismiss()
                            if (onNavigateToSignIn != null) {
                                onNavigateToSignIn()
                            } else {
                                navigator?.replaceAll(AppScreens.Welcome)
                            }
                        }
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onDismiss() }) {
                        Text("No")
                    }
                }
            )
        } else {
            AlertDialog(
                onDismissRequest = { onDismiss() },
                title = { Text("Error") },
                text = { Text(sanitizedMessage) },
                confirmButton = {
                    Button(onClick = { onDismiss() }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}
@Composable
fun SuccessAlertDialog(
    errorMessage: String?,
    onDismiss: () -> Unit
) {
    if (errorMessage != null) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text("Success") },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(onClick = { onDismiss() }) {
                    Text("OK")
                }
            }
        )
    }
}

package com.medical.buganddrug.util

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun ErrorAlertDialog(
    errorMessage: String?,
    onDismiss: () -> Unit
) {
    if (errorMessage != null) {
        println("Error: $errorMessage")

        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(onClick = { onDismiss() }) {
                    Text("OK")
                }
            }
        )
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

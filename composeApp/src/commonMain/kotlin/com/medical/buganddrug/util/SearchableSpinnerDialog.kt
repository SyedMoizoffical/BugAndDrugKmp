package com.medical.buganddrug.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.unit.dp
import buganddrug_multiplateform.composeapp.generated.resources.Res
import buganddrug_multiplateform.composeapp.generated.resources.arrow_drop_down
import org.jetbrains.compose.resources.painterResource

@Composable
fun <T> SearchableSpinnerDialog(
    label: String,
    items: List<T>,
    itemId: (T) -> Int,
    itemLabel: (T) -> String,
    selectedId: Int?,
    onItemSelected: (id: Int, label: String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var searchText by remember {
        mutableStateOf(
            items.find { itemId(it) == selectedId }?.let(itemLabel) ?: ""
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { showDialog = true } // ✅ open dialog anywhere
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            //.clickable { showDialog = true }, // ✅ open dialog on click
            shape = MaterialTheme.shapes.small,
            trailingIcon = {
                IconButton(onClick = { showDialog = true }) {
                    Icon(painter = painterResource(Res.drawable.arrow_drop_down),
                        contentDescription = "Select",
                        modifier = Modifier.size(24.dp) // actual icon size
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
    // Dialog
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    var searchQuery by remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Search $label") },
                        modifier = Modifier.fillMaxWidth()
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
                                    val id = itemId(item)
                                    val labelText = itemLabel(item)
                                    searchText = labelText
                                    onItemSelected(id, labelText)
                                    showDialog = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = itemLabel(item),
                                    textAlign = TextAlign.Left,
                                    modifier = Modifier.fillMaxWidth()
                                )                            }
                        }
                    }
                }
            }
        }
    }
}

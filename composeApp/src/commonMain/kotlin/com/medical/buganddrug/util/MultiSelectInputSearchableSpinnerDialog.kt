package com.medical.buganddrug.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import buganddrug_multiplateform.composeapp.generated.resources.Res
import buganddrug_multiplateform.composeapp.generated.resources.arrow_drop_down
import org.jetbrains.compose.resources.painterResource

@Composable
fun <T> MultiSelectInputSearchableSpinnerDialog(
    label: String,
    items: List<T>,
    itemId: (T) -> Int,
    itemLabel: (T) -> String,
    selectedIds: List<Int>,
    onSelectionChanged: (selected: List<Int>, selectedLabels: List<String>, extraInputs: Map<Int, String>) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedItems by remember { mutableStateOf(items.filter { itemId(it) in selectedIds }) }
    var extraInputs by remember { mutableStateOf<Map<Int, String>>(emptyMap()) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { showDialog = true } // ✅ open dialog anywhere
    ) {
    OutlinedTextField(
        value = selectedItems.joinToString(", ") { itemLabel(it) },
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
                    contentDescription = "Select")
            }
                       },
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = LocalContentColor.current.copy(alpha = 1f),
            disabledLabelColor = LocalContentColor.current.copy(alpha = 1f),
            disabledTrailingIconColor = LocalContentColor.current.copy(alpha = 1f),
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledContainerColor = MaterialTheme.colorScheme.surface
        )
    )}

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    var searchQuery by remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Search $label") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    val filteredItems = if (searchQuery.isEmpty()) items
                    else items.filter { itemLabel(it).contains(searchQuery, ignoreCase = true) }

                    // ✅ FIX: List gets its own scroll area
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp) // list won’t take full screen
                    ) {
                        items(filteredItems) { item ->
                            val isSelected = selectedItems.any { itemId(it) == itemId(item) }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = { checked ->
                                        val id = itemId(item)
                                        selectedItems = if (checked) {
                                            selectedItems + item
                                        } else {
                                            selectedItems.filter { itemId(it) != id }
                                        }

                                        // Update extraInputs properly (remove entry if unchecked)
                                        extraInputs = if (!checked) {
                                            extraInputs.toMutableMap().apply { remove(id) }
                                        } else {
                                            extraInputs
                                        }
                                    }
                                )
                                Text(
                                    text = itemLabel(item),
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }

                    // ✅ Input fields always stay below list
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        selectedItems.forEach { item ->
                            val id = itemId(item)
                            val labelText = itemLabel(item)
                            if (!extraInputs.containsKey(id)) {
                                extraInputs = extraInputs.toMutableMap().apply {
                                    this[id] = "1"
                                }
                            }
                            OutlinedTextField(
                                value = extraInputs[id] ?: "1",
                                onValueChange = { newValue ->
                                    extraInputs = extraInputs.toMutableMap().apply {
                                        this[id] = newValue
                                    }
                                },
                                label = { Text("No of days with $labelText") },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number
                                ),
                                maxLines = 1,
                                singleLine = true
                            )

                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = {
                                onSelectionChanged(
                                    selectedItems.map(itemId),
                                    selectedItems.map(itemLabel),
                                    extraInputs.toMap()
                                )
                                showDialog = false
                            }
                        ) { Text("OK") }
                    }
                }
            }
        }
    }
}


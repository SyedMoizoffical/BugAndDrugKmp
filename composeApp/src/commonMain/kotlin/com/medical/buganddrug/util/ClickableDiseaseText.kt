package com.medical.buganddrug.util

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.medical.buganddrug.data.model.LocalStorageDatamodel.DiseaseItem
import com.medical.buganddrug.ui.FilterScreen.ClinicalSyndromeFilterScreen
import com.medical.buganddrug.ui.FilterScreen.EtiologicalAgentFilterScreen
import com.medical.buganddrug.ui.QuickIDConsult.Q5.QuestionSixFilterScreen
import com.medical.buganddrug.ui.clinicalSyndrome.ClinicalSyndromeViewModel
import com.medical.buganddrug.ui.EtiologicalAgentScreen.EtiologicalAgentScreenViewModel
import com.medical.buganddrug.ui.QuickIDConsult.Q5.QuestionSixViewModel
import org.koin.compose.koinInject

/**
 * Custom Green color used for highlighting matched disease names in text.
 */
val DiseaseGreenColor = Color(0xFF2E7D32) // Soft deep green

/**
 * Composable that searches text for disease names in [diseaseList],
 * highlights matched disease names in GREEN, makes them CLICKABLE,
 * and opens a DiseaseDetailDialog on click.
 */
@Composable
fun ClickableDiseaseText(
    text: String,
    modifier: Modifier = Modifier,
    diseaseList: List<DiseaseItem> = emptyList(),
    style: TextStyle = LocalTextStyle.current,
    highlightColor: Color = DiseaseGreenColor,
    onDiseaseClick: ((DiseaseItem) -> Unit)? = null
) {
    var selectedDiseaseForDialog by remember { mutableStateOf<DiseaseItem?>(null) }

    val diseasesToSearch = remember(diseaseList, DiseaseExtractor.cachedDiseaseList) {
        if (diseaseList.isNotEmpty()) diseaseList else DiseaseExtractor.cachedDiseaseList
    }

    // Build AnnotatedString with green spans & clickable annotations for matched disease names
    val annotatedString = remember(text, diseasesToSearch, highlightColor) {
        buildAnnotatedDiseaseText(text, diseasesToSearch, highlightColor)
    }

    ClickableText(
        text = annotatedString,
        modifier = modifier,
        style = style,
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "DISEASE_KEY", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    val parts = annotation.item.split(":")
                    val type = parts.getOrNull(0) ?: "disease"
                    val diseaseId = parts.getOrNull(1)?.toIntOrNull() ?: 0
                    val name = parts.drop(2).joinToString(":")

                    val matchedDisease = diseasesToSearch.find {
                        it.name.equals(name, ignoreCase = true) && it.type.equals(type, ignoreCase = true) && it.id == diseaseId
                    } ?: diseasesToSearch.find {
                        it.name.equals(name, ignoreCase = true) && it.type.equals(type, ignoreCase = true)
                    } ?: diseasesToSearch.find {
                        it.name.equals(name, ignoreCase = true)
                    } ?: DiseaseItem(name = name, id = diseaseId, type = type)

                    if (onDiseaseClick != null) {
                        onDiseaseClick(matchedDisease)
                    } else {
                        selectedDiseaseForDialog = matchedDisease
                    }
                }
        }
    )

    // Popup dialog/overlay showing filter screens when clicked (preserves caller screen state and scroll position)
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

        if (disease.type.equals("disease", ignoreCase = true) ||
            disease.type.equals("organism", ignoreCase = true) ||
            disease.type.equals("organisum", ignoreCase = true) ||
            disease.type.equals("antibiotic", ignoreCase = true)
        ) {
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
        } else {
            DiseaseDetailDialog(
                disease = disease,
                onDismiss = { selectedDiseaseForDialog = null }
            )
        }
    }
}

/**
 * Helper function to build an AnnotatedString with green highlighted & annotated disease names.
 */
fun buildAnnotatedDiseaseText(
    fullText: String,
    diseaseList: List<DiseaseItem>,
    highlightColor: Color = DiseaseGreenColor
): AnnotatedString {
    if (fullText.isEmpty()) return buildAnnotatedString { }

    // Filter disease names present in fullText
    val matchedDiseases = diseaseList.filter { disease ->
        val name = disease.name.trim()
        name.isNotBlank() && fullText.contains(name, ignoreCase = true)
    }.sortedByDescending { it.name.length } // Match longer names first

    if (matchedDiseases.isEmpty()) {
        return buildAnnotatedString { append(fullText) }
    }

    // Find all match index ranges in the text
    data class MatchRange(val start: Int, val end: Int, val disease: DiseaseItem)
    val ranges = mutableListOf<MatchRange>()

    for (disease in matchedDiseases) {
        var startIndex = 0
        val lowerText = fullText.lowercase()
        val lowerName = disease.name.trim().lowercase()
        while (startIndex < fullText.length) {
            val foundIndex = lowerText.indexOf(lowerName, startIndex)
            if (foundIndex == -1) break
            val endIndex = foundIndex + lowerName.length

            // Check overlap with existing ranges
            val overlaps = ranges.any { r -> (foundIndex < r.end && endIndex > r.start) }
            if (!overlaps) {
                ranges.add(MatchRange(foundIndex, endIndex, disease))
            }
            startIndex = foundIndex + 1
        }
    }

    ranges.sortBy { it.start }

    return buildAnnotatedString {
        var currentIndex = 0
        for (match in ranges) {
            if (match.start > currentIndex) {
                append(fullText.substring(currentIndex, match.start))
            }
            // Add clickable annotation tag with type, id, and name
            val payload = "${match.disease.type}:${match.disease.id}:${match.disease.name}"
            pushStringAnnotation(tag = "DISEASE_KEY", annotation = payload)
            withStyle(
                style = SpanStyle(
                    color = highlightColor,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(fullText.substring(match.start, match.end))
            }
            pop()
            currentIndex = match.end
        }
        if (currentIndex < fullText.length) {
            append(fullText.substring(currentIndex))
        }
    }
}

/**
 * Dialog displaying information for a clicked disease (Name, ID, Type).
 */
@Composable
fun DiseaseDetailDialog(
    disease: DiseaseItem,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    color = DiseaseGreenColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "ID: ${disease.id}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = DiseaseGreenColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
                Text(
                    text = disease.name,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Divider()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = when (disease.type) {
                            "organism" -> "Organism Name:"
                            "antibiotic" -> "Antibiotic Name:"
                            else -> "Disease Name:"
                        },
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = disease.name,
                        fontWeight = FontWeight.Bold,
                        color = DiseaseGreenColor
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "ID:",
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${disease.id}",
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Type:",
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = disease.type.ifBlank { "disease" },
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = DiseaseGreenColor, fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

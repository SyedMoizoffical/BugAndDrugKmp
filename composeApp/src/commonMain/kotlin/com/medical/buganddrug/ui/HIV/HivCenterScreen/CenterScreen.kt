package com.medical.buganddrug.ui.HIV.HivCenterScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import buganddrug_multiplateform.composeapp.generated.resources.ExpandLess
import buganddrug_multiplateform.composeapp.generated.resources.Res
import buganddrug_multiplateform.composeapp.generated.resources.arrow_drop_down
import buganddrug_multiplateform.composeapp.generated.resources.left_arrow
import com.medical.buganddrug.data.model.hivCenterModel.HivArtCenter
import org.jetbrains.compose.resources.painterResource

sealed class CenterUiLevel {
    object Province : CenterUiLevel()
    data class City(val province: String) : CenterUiLevel()
    data class Center(val province: String, val city: String) : CenterUiLevel()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenterScreen(
    viewModel: HivArtCenterViewModel ,
    onBackClick: () -> Unit = {},
    type: String
) {
    val centers by viewModel.centers.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val filteredCenters = centers.filter { it.type == type }

    var level by remember { mutableStateOf<CenterUiLevel>(CenterUiLevel.Province) }

    LaunchedEffect(Unit) {
        viewModel.loadCenters()
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (level) {
                        CenterUiLevel.Province -> "Select Province"
                        is CenterUiLevel.City -> "Select City"
                        is CenterUiLevel.Center -> "Treatment Centres"
                    },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {  level = when (level) {
                    is CenterUiLevel.Center -> {
                        CenterUiLevel.City((level as CenterUiLevel.Center).province)
                    }
                    is CenterUiLevel.City -> CenterUiLevel.Province
                    CenterUiLevel.Province -> {
                        onBackClick()
                        CenterUiLevel.Province
                    }
                } }) {
                    Icon(
                        painter = painterResource(Res.drawable.left_arrow),

                        contentDescription = "Back")
                }
            }
        }

,        containerColor = Color(0xFFF6F7FB)
    ) { padding ->

        Box(modifier = Modifier.padding(padding)) {

            when {
                loading -> LoadingView()
                error != null -> ErrorView(error!!) {
                    viewModel.clearError()
                    viewModel.loadCenters()
                }

                else -> {
                    when (val current = level) {

                        CenterUiLevel.Province -> {
                            ProvinceList(
                                provinces = filteredCenters.map { it.province }.distinct(),
                                onSelect = { level = CenterUiLevel.City(it) }
                            )
                        }

                        is CenterUiLevel.City -> {
                            CityList(
                                cities = filteredCenters
                                    .filter { it.province == current.province }
                                    .mapNotNull { it.city }
                                    .distinct(),
                                onSelect = {
                                    level = CenterUiLevel.Center(current.province, it)
                                }
                            )
                        }

                        is CenterUiLevel.Center -> {
                            CenterList(
                                centers = filteredCenters.filter {
                                    it.province == current.province &&
                                            it.city == current.city
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProvinceList(
    provinces: List<String>,
    onSelect: (String) -> Unit
) {
    SelectionList(
        items = provinces,
        iconColor = Color(0xFF1565C0),
        onSelect = onSelect
    )
}

@Composable
fun CityList(
    cities: List<String>,
    onSelect: (String) -> Unit
) {
    SelectionList(
        items = cities,
        iconColor = Color(0xFF2E7D32),
        onSelect = onSelect
    )
}
@Composable
fun SelectionList(
    items: List<String>,
    iconColor: Color,
    onSelect: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items) { item ->
            ExpandableSelectionCard(
                title = item,
                iconColor = iconColor,
                onSelect = { onSelect(item) }
            )
        }
    }
}

@Composable
fun ExpandableSelectionCard(
    title: String,
    iconColor: Color,
    onSelect: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .clickable {
                    expanded = !expanded
                    onSelect() // optional: remove if you don't want auto navigation
                }
                .padding(16.dp)
        ) {

            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    painter = painterResource(
                        if (expanded)
                            Res.drawable.ExpandLess
                        else
                            Res.drawable.arrow_drop_down
                    ),
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp) // actual icon size

                )
            }

            // Expandable content
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {

                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Tap again to collapse",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}


@Composable
fun CenterList(centers: List<HivArtCenter>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(centers) { center ->
            Card(
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(16.dp).fillMaxWidth()

                ) {

                    Text(
                        text = center.center,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "${center.city}, ${center.province}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}


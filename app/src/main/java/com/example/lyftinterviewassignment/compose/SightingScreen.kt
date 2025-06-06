package com.example.lyftinterviewassignment.compose

// SightingScreen.kt
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.lyftinterviewassignment.R
import com.example.lyftinterviewassignment.model.SightingType
import com.example.lyftinterviewassignment.model.UFOSighting
import com.example.lyftinterviewassignment.state.SightingUiState
import com.example.lyftinterviewassignment.viewmodel.SightingViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SightingListScreen(viewModel: SightingViewModel = androidx.hilt.navigation.compose.hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "UFO Sightings",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black)
                },
                actions = {
                    IconButton(onClick = {viewModel.addRandomSighting()}) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add UFO Sighting",
                            tint = Color(0xFF08A462)
                        )
                    }
                }
            )
        }
    ) { padding ->
        when (val state = uiState) {
            is SightingUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    androidx.compose.material3.CircularProgressIndicator()
                }
            }

            is SightingUiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(state.message)
                }
            }

            is SightingUiState.Success -> {
                LazyColumn(modifier = Modifier.padding(padding)) {
                    items(state.sightings) { sighting ->
                        var showRemove by remember { mutableStateOf(false) }
                        SightingItem(
                            sighting = sighting,
                            onSelected = { showRemove = !showRemove },
                            onRemove = { viewModel.removeSighting(sighting.id) },
                            showRemove = showRemove
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SightingItem(
    sighting: UFOSighting,
    onSelected: () -> Unit,
    onRemove: () -> Unit,
    showRemove: Boolean
) {
    Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 64.dp)
                .padding(vertical = 10.dp)
                .clickable { onSelected() },
    verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(64.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = when (sighting.type) {
                    SightingType.BLOB -> R.drawable.blob_medium // your blob icon
                    SightingType.LAMPSHADE -> R.drawable.lampshade_medium // your lampshade icon
                }),
                contentDescription = null,
                tint = Color(0xFF08A462),
                modifier = Modifier.size(28.dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy @ h:mm a")
            Text(
                text = sighting.date.format(formatter),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color.Black
            )
            Text(
                text = "${sighting.speed.toInt()} knots · ${if (sighting.type == SightingType.BLOB) "Blob" else "Lamp Shade"}",
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.6f)
            )
        }
            if (showRemove) {
                androidx.compose.material3.Button(onClick = onRemove) {
                    androidx.compose.material3.Text("Remove")
                }
            }
        }
    }


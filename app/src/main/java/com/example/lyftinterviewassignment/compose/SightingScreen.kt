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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.lyftinterviewassignment.R
import com.example.lyftinterviewassignment.model.SightingType
import com.example.lyftinterviewassignment.model.UFOSighting
import com.example.lyftinterviewassignment.state.SightingUiState
import com.example.lyftinterviewassignment.viewmodel.SightingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SightingListScreen(viewModel: SightingViewModel = androidx.hilt.navigation.compose.hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "UFO Sightings", fontWeight = FontWeight.Bold)
                },
                actions = {
                    IconButton(onClick = {viewModel.addRandomSighting()}) {
                        Icon(Icons.Default.Add, "Add")
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
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onSelected() }
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val painter: Painter = when (sighting.type) {
                    SightingType.BLOB -> painterResource(id = R.drawable.blob_medium) // Example
                    SightingType.LAMPSHADE -> painterResource(id = R.drawable.lampshade_medium) // Example
                }
                Image(
                    painter = painter,
                    contentDescription = "Type"
                )
                Spacer(Modifier.width(8.dp))
                androidx.compose.material3.Text("${sighting.date}")
            }
            androidx.compose.material3.Text("${sighting.speed} Knots")
            androidx.compose.material3.Text("${sighting.type}")
            if (showRemove) {
                androidx.compose.material3.Button(onClick = onRemove) {
                    androidx.compose.material3.Text("Remove")
                }
            }
        }
    }
}

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
import androidx.compose.ui.res.painterResource
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
                    Text(text = "UFO Sightings")
                },
                actions = {
                    IconButton(onClick = {viewModel.addRandomSighting()}) {
                        Icon(Icons.Default.Add, "Add")
                    }
                }
            )
        }
//        floatingActionButton = {
//            FloatingActionButton(onClick = { viewModel.addRandomSighting() }) {
//                Icon(Icons.Default.Add, contentDescription = "Add")
//            }
//        }
    ) { padding ->
        when (uiState) {
            is SightingUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is SightingUiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text((uiState as SightingUiState.Error).message)
                }
            }
            is SightingUiState.Success -> {
                val sightings = (uiState as SightingUiState.Success).sightings
                LazyColumn(modifier = Modifier.padding(padding)) {
                    items(sightings) { sighting ->
                        var showRemove by remember { mutableStateOf(false) }
                        Text(text = "this")
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
                Image(
                    painter = painterResource(
                        id = when (sighting.type) {
                            SightingType.BLOB -> R.drawable.blob_small
                            SightingType.LAMPSHADE -> R.drawable.lampshade_small
                        }
                    ),
                    contentDescription = "Type"
                )
                Spacer(Modifier.width(8.dp))
                Text("Type: ${sighting.type}")
            }
            Text("Date: ${sighting.date}")
            Text("Speed: ${sighting.speed}")
            if (showRemove) {
                Button(onClick = onRemove) {
                    Text("Remove")
                }
            }
        }
    }
}

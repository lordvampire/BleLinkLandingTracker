package com.davidrevolt.blelink.ui.data

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DataScreen(
    deviceAddress: String,
    characteristicUuid: String,
    viewModel: DataViewModel = hiltViewModel()
) {
    val characteristicData by viewModel.characteristicData.collectAsState()

    // Observe the characteristic data for the specific characteristic UUID
    viewModel.observeCharacteristic(deviceAddress, characteristicUuid)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Display the characteristic data in a large text format
        Text(
            text = characteristicData,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
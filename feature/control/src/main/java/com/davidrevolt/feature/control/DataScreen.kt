package com.davidrevolt.feature.control

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DataScreen(
    deviceAddress: String,
    characteristicUuid: String,
    viewModel: DataViewModel = hiltViewModel<DataViewModel>()
) {
    val characteristicData by viewModel.characteristicData.collectAsStateWithLifecycle()

 Box(
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Display the characteristic data in a large text format
        Text(
            text = characteristicData,
            fontSize = 60.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
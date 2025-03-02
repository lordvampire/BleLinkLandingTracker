package com.davidrevolt.feature.control

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.davidrevolt.core.ble.model.CustomGattCharacteristic
import com.davidrevolt.core.ble.model.CustomGattDescriptor
import com.davidrevolt.core.ble.model.CustomGattService
import com.davidrevolt.core.ble.model.PropertiesAsEnum
import java.util.UUID


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ControlScreen(
    viewModel: ControlViewModel = hiltViewModel()
) {
    val uiState by viewModel.controlUiState.collectAsStateWithLifecycle()
    //  val connectToDeviceGatt = viewModel::connectToDeviceGatt
    // val disconnectFromGatt = viewModel::disconnectFromGatt
    val onReadCharacteristic = viewModel::readCharacteristic
    val onWriteCharacteristic = viewModel::writeCharacteristic
    val onReadDescriptor = viewModel::readDescriptor
    val onWriteDescriptor = viewModel::writeDescriptor
    val onEnableCharacteristicNotifications = viewModel::enableCharacteristicNotifications

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is ControlUiState.Data -> {
                val data = (uiState as ControlUiState.Data)
                ControlScreenContent(
                    deviceAddress = data.deviceAddress,
                    connectionState = data.connectionState,
                    deviceServices = data.deviceServices,
                    onReadCharacteristic = onReadCharacteristic,
                    onWriteCharacteristic = onWriteCharacteristic,
                    onReadDescriptor = onReadDescriptor,
                    onWriteDescriptor = onWriteDescriptor,
                    onEnableCharacteristicNotifications = onEnableCharacteristicNotifications
                )
            }

            is ControlUiState.Loading -> CircularProgressIndicator()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControlScreenContent(
    deviceAddress: String,
    connectionState: Int,
    deviceServices: List<CustomGattService>,
    onReadCharacteristic: (characteristicUUID: UUID) -> Unit,
    onWriteCharacteristic: (characteristicUUID: UUID, value: ByteArray) -> Unit,
    onReadDescriptor: (characteristicUUID: UUID, descriptorUUID: UUID) -> Unit,
    onWriteDescriptor: (characteristicUUID: UUID, descriptorUUID: UUID, value: ByteArray) -> Unit,
    onEnableCharacteristicNotifications: (characteristicUUID: UUID) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Device Control - $deviceAddress") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = { //TODO - BACK BUTTON
                    IconButton(onClick = { /*navController.popBackStack()*/ }) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.bluetooth),
                        contentDescription = "Connection",
                        tint = when (connectionState) {
                            2 -> MaterialTheme.colorScheme.primary
                            0 -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = when (connectionState) {
                            0 -> "Disconnected"
                            1 -> "Connecting..."
                            2 -> "Connected"
                            3 -> "Disconnecting..."
                            else -> "Unknown connection state!!"
                        },
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            if (connectionState == 2) { // CONNECTED
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    deviceServices.forEach { service ->
                        item {
                            ServiceItem(
                                service = service,
                                onReadCharacteristic = onReadCharacteristic,
                                onWriteCharacteristic = onWriteCharacteristic,
                                onReadDescriptor = onReadDescriptor,
                                onWriteDescriptor = onWriteDescriptor,
                                onEnableCharacteristicNotifications = onEnableCharacteristicNotifications
                            )
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun ServiceItem(
    service: CustomGattService,
    onReadCharacteristic: (characteristicUUID: UUID) -> Unit,
    onWriteCharacteristic: (characteristicUUID: UUID, value: ByteArray) -> Unit,
    onReadDescriptor: (characteristicUUID: UUID, descriptorUUID: UUID) -> Unit,
    onWriteDescriptor: (characteristicUUID: UUID, descriptorUUID: UUID, value: ByteArray) -> Unit,
    onEnableCharacteristicNotifications: (characteristicUUID: UUID) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = service.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface, // Bold and prominent
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = if (expanded) painterResource(R.drawable.expand_less) else painterResource(
                        R.drawable.expand_more
                    ),
                    contentDescription = "Expand",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            if (expanded) {
                service.characteristics.forEach { characteristic ->
                    CharacteristicItem(
                        characteristic = characteristic,
                        onReadCharacteristic = onReadCharacteristic,
                        onWriteCharacteristic = onWriteCharacteristic,
                        onReadDescriptor = onReadDescriptor,
                        onWriteDescriptor = onWriteDescriptor,
                        onEnableCharacteristicNotifications = onEnableCharacteristicNotifications
                    )
                }
            }
        }
    }
}


/*
* Custom enum for input mode to send characteristic/descriptor bytearray data
* Text mode: "Hello" → [72, 101, 108, 108, 111] (UTF-8).
* Hex mode: "0200" → [0x02, 0x00].
* */

enum class CustomInputMode {
    TEXT, HEX
}

@Composable
fun CharacteristicItem(
    characteristic: CustomGattCharacteristic,
    onReadCharacteristic: (characteristicUUID: UUID) -> Unit,
    onWriteCharacteristic: (characteristicUUID: UUID, value: ByteArray) -> Unit,
    onReadDescriptor: (characteristicUUID: UUID, descriptorUUID: UUID) -> Unit,
    onWriteDescriptor: (characteristicUUID: UUID, descriptorUUID: UUID, value: ByteArray) -> Unit,
    onEnableCharacteristicNotifications: (characteristicUUID: UUID) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var writeValue by remember { mutableStateOf("") }
    var inputMode by remember { mutableStateOf(CustomInputMode.TEXT) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = characteristic.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = if (expanded) painterResource(R.drawable.expand_less) else painterResource(
                    R.drawable.expand_more
                ),
                contentDescription = "Expand",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        if (expanded) {
            Text(
                text = "UUID: ${characteristic.uuid}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Properties: ${characteristic.properties.joinToString { it.name }}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            characteristic.readBytes?.let {
                Text(
                    text = "Value: ${it.joinToString(" ") { byte -> String.format("%02X", byte) }}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (characteristic.readable) {
                        Button(onClick = { onReadCharacteristic(characteristic.uuid)}, shape = RoundedCornerShape(8.dp)) {
                            Text("Read")
                        }
                    }
                    if (characteristic.writable) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = writeValue,
                                    onValueChange = {
                                        writeValue = if (inputMode == CustomInputMode.HEX) {
                                            it.filter { char -> char.isDigit() || "abcdefABCDEF".contains(char) }
                                        } else {
                                            it
                                        }
                                    },
                                    label = {
                                        Text(if (inputMode == CustomInputMode.TEXT) "Enter text" else "Enter hex (e.g., 0200)")
                                    },
                                    placeholder = {
                                        Text(if (inputMode == CustomInputMode.TEXT) "e.g., Hello" else "e.g., 0200")
                                    },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = if (inputMode == CustomInputMode.HEX) KeyboardType.Ascii else KeyboardType.Text
                                    )
                                )
                                Button(
                                    onClick = {
                                        val bytes = when (inputMode) {
                                            CustomInputMode.TEXT -> writeValue.toByteArray(Charsets.UTF_8)
                                            CustomInputMode.HEX -> {
                                                try {
                                                    hexStringToByteArray(writeValue)
                                                } catch (e: Exception) {
                                                    byteArrayOf()
                                                }
                                            }
                                        }
                                        if (bytes.isNotEmpty()) {
                                            onWriteCharacteristic(characteristic.uuid, bytes)
                                            writeValue = ""
                                        }
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    enabled = writeValue.isNotBlank()
                                ) {
                                    Text("Write")
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Input Mode:",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Button(
                                    onClick = { inputMode = CustomInputMode.TEXT },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (inputMode == CustomInputMode.TEXT) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                        contentColor = if (inputMode == CustomInputMode.TEXT) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Text")
                                }
                                Button(
                                    onClick = { inputMode = CustomInputMode.HEX },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (inputMode == CustomInputMode.HEX) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                        contentColor = if (inputMode == CustomInputMode.HEX) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Hex")
                                }
                            }
                        }
                    }
                    if (characteristic.properties.contains(PropertiesAsEnum.PROPERTY_NOTIFY) ||
                        characteristic.properties.contains(PropertiesAsEnum.PROPERTY_INDICATE)) {
                        Button(
                            onClick = { onEnableCharacteristicNotifications(characteristic.uuid)},
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Notify")
                        }
                    }
                }
            }
            characteristic.descriptors.forEach { descriptor ->
                DescriptorItem(
                    characteristic = characteristic,
                    descriptor = descriptor,
                    onReadDescriptor = onReadDescriptor,
                    onWriteDescriptor = onWriteDescriptor,
                )
            }
        }
    }
}

@Composable
fun DescriptorItem(
    characteristic: CustomGattCharacteristic,
    descriptor: CustomGattDescriptor,
    onReadDescriptor: (characteristicUUID: UUID, descriptorUUID: UUID) -> Unit,
    onWriteDescriptor: (characteristicUUID: UUID, descriptorUUID: UUID, value: ByteArray) -> Unit,
) {
    var writeValue by remember { mutableStateOf("") }
    var inputMode by remember { mutableStateOf(CustomInputMode.TEXT) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp, end = 16.dp)
    ) {
        Text(
            text = "Descriptor: ${descriptor.name}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Text(
            text = "UUID: ${descriptor.uuid}",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        Text(
            text = "Permissions: ${descriptor.permissions.joinToString { it.name }}",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        descriptor.readBytes?.let {
            Text(
                text = "Value: ${it.joinToString(" ") { byte -> String.format("%02X", byte) }}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        Column(
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (descriptor.readable) {
                    Button(       onClick = { onReadDescriptor(characteristic.uuid, descriptor.uuid) }, shape = RoundedCornerShape(8.dp)) {
                        Text("Read")
                    }
                }
                if (descriptor.writable) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = writeValue,
                                onValueChange = {
                                    writeValue = if (inputMode == CustomInputMode.HEX) {
                                        it.filter { char -> char.isDigit() || "abcdefABCDEF".contains(char) }
                                    } else {
                                        it
                                    }
                                },
                                label = {
                                    Text(if (inputMode == CustomInputMode.TEXT) "Enter text" else "Enter hex (e.g., 0200)")
                                },
                                placeholder = {
                                    Text(if (inputMode == CustomInputMode.TEXT) "e.g., Hello" else "e.g., 0200")
                                },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = if (inputMode == CustomInputMode.HEX) KeyboardType.Ascii else KeyboardType.Text
                                )
                            )
                            Button(
                                onClick = {
                                    val bytes = when (inputMode) {
                                        CustomInputMode.TEXT -> writeValue.toByteArray(Charsets.UTF_8)
                                        CustomInputMode.HEX -> {
                                            try {
                                                hexStringToByteArray(writeValue)
                                            } catch (e: Exception) {
                                                byteArrayOf()
                                            }
                                        }
                                    }
                                    if (bytes.isNotEmpty()) {
                                        onWriteDescriptor(characteristic.uuid, descriptor.uuid, bytes)
                                        writeValue = ""
                                    }
                                },
                                shape = RoundedCornerShape(8.dp),
                                enabled = writeValue.isNotBlank()
                            ) {
                                Text("Write")
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Input Mode:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                            Button(
                                onClick = { inputMode = CustomInputMode.TEXT },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (inputMode == CustomInputMode.TEXT) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = if (inputMode == CustomInputMode.TEXT) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Text")
                            }
                            Button(
                                onClick = { inputMode = CustomInputMode.HEX },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (inputMode == CustomInputMode.HEX) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = if (inputMode == CustomInputMode.HEX) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Hex")
                            }
                        }
                    }
                }
            }
        }
    }
}


// Helper function to convert hex string to ByteArray
fun hexStringToByteArray(hex: String): ByteArray {
    val cleanHex = hex.replace(" ", "").trim()
    require(cleanHex.length % 2 == 0) { "Hex string must have an even number of characters" }
    require(cleanHex.all { it.isDigit() || "abcdefABCDEF".contains(it) }) { "Invalid hex characters" }
    return ByteArray(cleanHex.length / 2) { i ->
        cleanHex.substring(i * 2, i * 2 + 2).toInt(16).toByte()
    }
}

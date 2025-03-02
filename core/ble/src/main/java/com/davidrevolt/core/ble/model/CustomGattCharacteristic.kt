package com.davidrevolt.core.ble.model

import java.util.UUID

data class CustomGattCharacteristic(
    val uuid: UUID,
    val name: String,
    val properties: List<PropertiesAsEnum>, // Doesn't mean we have permission to use the prop
    val readable: Boolean,
    val writable: Boolean,
    val readBytes: ByteArray?,
    val descriptors: List<CustomGattDescriptor>
)



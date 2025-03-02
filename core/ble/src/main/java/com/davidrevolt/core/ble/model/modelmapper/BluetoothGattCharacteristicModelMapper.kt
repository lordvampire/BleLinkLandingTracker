package com.davidrevolt.core.ble.model.modelmapper

import android.bluetooth.BluetoothGattCharacteristic
import com.davidrevolt.core.ble.model.CustomGattCharacteristic
import com.davidrevolt.core.ble.util.BleNamesResolver.containsProperty
import com.davidrevolt.core.ble.util.BleNamesResolver.propertiesAsList
import com.davidrevolt.core.ble.util.BleNamesResolver.resolveUuid

/*
* Convert BluetoothGattCharacteristic to CustomGattCharacteristics which is more readable Characteristic obj
*/

fun BluetoothGattCharacteristic.asCustomDeviceCharacteristics() =
    CustomGattCharacteristic(
        uuid = this.uuid,
        name = this.resolveUuid(),
        properties = this.propertiesAsList(),
        readable = this.containsProperty(BluetoothGattCharacteristic.PROPERTY_READ),
        writable = this.containsProperty(BluetoothGattCharacteristic.PROPERTY_WRITE) ||
                this.containsProperty(BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE),
        readBytes = null,
        descriptors = this.descriptors.map { bluetoothGattDescriptor -> bluetoothGattDescriptor.asCustomGattDescriptor() }
    )
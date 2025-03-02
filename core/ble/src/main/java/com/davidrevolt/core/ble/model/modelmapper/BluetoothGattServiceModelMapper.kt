package com.davidrevolt.core.ble.model.modelmapper

import android.bluetooth.BluetoothGattService
import com.davidrevolt.core.ble.model.CustomGattService
import com.davidrevolt.core.ble.util.BleNamesResolver.resolveUuid


//TODO: Crate SERVICE UUID TO NAME CONVERTER
fun BluetoothGattService.asCustomGattService() =
    CustomGattService(
        uuid = this.uuid,
        name = this.resolveUuid(),
        characteristics = this.characteristics.map { bluetoothGattCharacteristic -> bluetoothGattCharacteristic.asCustomDeviceCharacteristics() }
    )
package com.davidrevolt.core.ble.model.modelmapper

import android.bluetooth.BluetoothGattDescriptor
import com.davidrevolt.core.ble.model.CustomGattDescriptor
import com.davidrevolt.core.ble.util.BleNamesResolver.containsPermission
import com.davidrevolt.core.ble.util.BleNamesResolver.containsProperty
import com.davidrevolt.core.ble.util.BleNamesResolver.permissionsAsList
import com.davidrevolt.core.ble.util.BleNamesResolver.resolveUuid

fun BluetoothGattDescriptor.asCustomGattDescriptor() =
    CustomGattDescriptor(
        uuid = this.uuid,
        name = this.resolveUuid(),
        permissions = this.permissionsAsList(), //TODO: FIX THAT
        readable = this.containsPermission(BluetoothGattDescriptor.PERMISSION_READ) || this.containsPermission(
            BluetoothGattDescriptor.PERMISSION_READ_ENCRYPTED) || this.containsPermission(
                BluetoothGattDescriptor.PERMISSION_READ_ENCRYPTED_MITM)
        ,
        writable = this.containsPermission(BluetoothGattDescriptor.PERMISSION_WRITE) || this.containsPermission(
            BluetoothGattDescriptor.PERMISSION_WRITE_ENCRYPTED) || this.containsPermission(
                BluetoothGattDescriptor.PERMISSION_WRITE_ENCRYPTED_MITM) || this.containsPermission(
            BluetoothGattDescriptor.PERMISSION_WRITE_SIGNED)|| this.containsPermission(
            BluetoothGattDescriptor.PERMISSION_WRITE_SIGNED_MITM)
        ,
        readBytes = null,
    )
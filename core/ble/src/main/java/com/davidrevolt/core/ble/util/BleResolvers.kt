package com.davidrevolt.core.ble.util

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import com.davidrevolt.core.ble.model.PermissionsAsEnum
import com.davidrevolt.core.ble.model.PropertiesAsEnum
import java.util.UUID


object BleNamesResolver {
    private var knownServices: MutableMap<UUID, String>
    private var knownCharacteristics: MutableMap<UUID, String>
    private var knownDescriptors: MutableMap<UUID, String>
    init {

        // Xiaomi Mi Band 4c Services:
        knownServices = mutableMapOf(
            UUID.fromString("00001800-0000-1000-8000-00805f9b34fb") to "Generic Access",
            UUID.fromString("00001801-0000-1000-8000-00805f9b34fb") to "Generic Attribute",
            UUID.fromString("16187f00-0000-1000-8000-00807f9b34fb") to "Xiaomi Wear Service - Mi Smart Watch 4C/Redmi Band",
        )


        // Xiaomi Mi Band 4c Characteristic:
        knownCharacteristics = mutableMapOf(
            UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb") to "Device Name",
            UUID.fromString("00002a01-0000-1000-8000-00805f9b34fb") to "Appearance",
            UUID.fromString("00002a04-0000-1000-8000-00805f9b34fb") to "Peripheral Preferred Connection Parameters",
            UUID.fromString("00002aa6-0000-1000-8000-00805f9b34fb") to "Central Address Resolution",

            UUID.fromString("16187f01-0000-1000-8000-00807f9b34fb") to "Likely the authentication/control characteristic",
            UUID.fromString("16187f02-0000-1000-8000-00807f9b34fb") to "Might be for encrypted communication",
            UUID.fromString("16187f03-0000-1000-8000-00807f9b34fb") to "Could be used for notifications/responses",
            UUID.fromString("16187f04-0000-1000-8000-00807f9b34fb") to "Might be related to data retrieval",
        )

        knownDescriptors = mutableMapOf()
    }

    /**
     * Convert The BluetoothGattService.UUID TO Readable Name
     * */
    fun BluetoothGattService.resolveUuid() =
        knownServices[uuid] ?: "Unknown Service"

    /**
     * Convert The Characteristic.UUID TO Readable Name
     * */
    fun BluetoothGattCharacteristic.resolveUuid() =
        knownCharacteristics[uuid] ?: "Unknown Characteristic"

    /**
     * Convert The Descriptor.UUID TO Readable Name
     * */
    fun BluetoothGattDescriptor.resolveUuid() =
        knownDescriptors[uuid] ?: "Unknown Descriptor"

    /**
     * Convert Characteristic.properties value to list of all the supported properties as enum
     */
    fun BluetoothGattCharacteristic.propertiesAsList(): List<PropertiesAsEnum> {
        val propertyList = mutableListOf<PropertiesAsEnum>()
        PropertiesAsEnum.entries.forEach { propertiesAsEnum ->
            if (this.containsProperty(propertiesAsEnum.value))
                propertyList.add(propertiesAsEnum)
        }
        return propertyList
    }

    /**
     * Return if Characteristic contains property
     * e.g: BluetoothGattCharacteristic.containsProperty(BluetoothGattCharacteristic.PROPERTY_READ)
     * */
    fun BluetoothGattCharacteristic.containsProperty(property: Int): Boolean =
        properties and property != 0


    /**
     * Convert Descriptor.permissions value to list of all the supported properties as enum
     */
    fun BluetoothGattDescriptor.permissionsAsList(): List<PermissionsAsEnum> {
        val permissionsList = mutableListOf<PermissionsAsEnum>()
        PermissionsAsEnum.entries.forEach { permissionsAsEnum ->
            if (this.containsPermission(permissionsAsEnum.value))
                permissionsList.add(permissionsAsEnum)
        }
        return permissionsList
    }

    /**
     * Return if Descriptor contains permission
     * e.g: Descriptor.containsProperty(BluetoothGattDescriptor.PERMISSION_READ)
     * */
    fun BluetoothGattDescriptor.containsPermission(permission: Int): Boolean =
        permissions and permission != 0

}

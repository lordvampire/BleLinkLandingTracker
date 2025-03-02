package com.davidrevolt.core.ble.model.modelmapper

import android.bluetooth.le.ScanResult
import androidx.annotation.RequiresPermission
import com.davidrevolt.core.ble.model.CustomScanResult
import android.Manifest
import android.util.SparseArray

@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
fun ScanResult.asCustomScanResult() =
    CustomScanResult(
        name = this.device.name ?: "UnKnown",
        address = this.device.address,
        rssi = this.rssi,
        manufacturer = getManufacturerName(this.scanRecord?.manufacturerSpecificData)
    )

fun getManufacturerName(manufacturerSpecificData: SparseArray<ByteArray>?): String? {
    val manufacturerId = getManufacturerId(manufacturerSpecificData)
    val name = manufacturerId?.let {
        manufacturerId.toString()
    }
    return name
}

fun getManufacturerId(manufacturerSpecificData: SparseArray<ByteArray>?): Int? {
    var mfId: Int? = null
    manufacturerSpecificData?.let {
        for (i in 0 until manufacturerSpecificData.size()) {
            mfId = manufacturerSpecificData.keyAt(i)
        }
    }
    return mfId
}
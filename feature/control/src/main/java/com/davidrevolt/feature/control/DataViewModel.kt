package com.davidrevolt.feature.control

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidrevolt.core.ble.BluetoothLeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val bluetoothLeService: BluetoothLeService
) : ViewModel() {

    private val deviceAddress: String = checkNotNull(savedStateHandle["deviceAddress"])
    private val characteristicUuid: String = checkNotNull(savedStateHandle["characteristicUuid"])

    private val _characteristicData = MutableStateFlow<String?>(null)
    val characteristicData: StateFlow<String?> = _characteristicData.asStateFlow()

    init {
        viewModelScope.launch {
            bluetoothLeService.getCharacteristicData(deviceAddress, UUID.fromString(characteristicUuid)).collect { data ->
                _characteristicData.value = data
            }
        }
    }
}
package com.example.clock

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeParseException

class ClockInViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {

    private val _clockInTime = MutableStateFlow<LocalTime?>(null)
    val clockInTime: StateFlow<LocalTime?> = _clockInTime.asStateFlow()

    private val _clockOutTime = MutableStateFlow<LocalTime?>(null)
    val clockOutTime: StateFlow<LocalTime?> = _clockOutTime.asStateFlow()

    init {
        // Collect clock-in time from DataStore
        viewModelScope.launch {
            dataStoreManager.getClockInTime().collect { timeString ->
                _clockInTime.value = if (!timeString.isNullOrEmpty()) {
                    try {
                        LocalTime.parse(timeString)
                    } catch (e: DateTimeParseException) {
                        Log.e("ClockInViewModel", "Failed to parse clock-in time: ${e.message}")
                        null
                    }
                } else {
                    null
                }
            }
        }

        // Collect clock-out time from DataStore
        viewModelScope.launch {
            dataStoreManager.getClockOutTime().collect { timeString ->
                _clockOutTime.value = if (!timeString.isNullOrEmpty()) {
                    try {
                        LocalTime.parse(timeString)
                    } catch (e: DateTimeParseException) {
                        Log.e("ClockInViewModel", "Failed to parse clock-out time: ${e.message}")
                        null
                    }
                } else {
                    null
                }
            }
        }
    }

    fun clockIn() {
        val currentTime = LocalTime.now()
        _clockInTime.value = currentTime
        _clockOutTime.value = null // Reset clock-out time
        viewModelScope.launch {
            dataStoreManager.saveClockInTime(currentTime.toString())
            dataStoreManager.saveClockOutTime(null) // Clear clock-out time
        }
    }

    fun clockOut() {
        val currentTime = LocalTime.now()
        _clockOutTime.value = currentTime
        viewModelScope.launch {
            dataStoreManager.saveClockOutTime(currentTime.toString())
        }
    }
}
package com.example.clock

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

class ClockInViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {

    private val _clockInTime = MutableStateFlow<LocalTime?>(null)
    val clockInTime: StateFlow<LocalTime?> = _clockInTime.asStateFlow()

    private val _clockOutTime = MutableStateFlow<LocalTime?>(null)
    val clockOutTime: StateFlow<LocalTime?> = _clockOutTime.asStateFlow()

    private val _customClockInTime = MutableStateFlow<LocalTime?>(null)
    val customClockInTime: StateFlow<LocalTime?> = _customClockInTime.asStateFlow()

    private val _customClockInInput = MutableStateFlow("")
    val customClockInInput: StateFlow<String> = _customClockInInput.asStateFlow()

    private val _lunchClockInTime = MutableStateFlow<LocalTime?>(null)
    val lunchClockInTime: StateFlow<LocalTime?> = _lunchClockInTime.asStateFlow()

    private val _lunchClockOutTime = MutableStateFlow<LocalTime?>(null)
    val lunchClockOutTime: StateFlow<LocalTime?> = _lunchClockOutTime.asStateFlow()

    private val _expectedLunchBackTime = MutableStateFlow<LocalTime?>(null)
    val expectedLunchBackTime: StateFlow<LocalTime?> = _expectedLunchBackTime.asStateFlow()

    private val _extraLunchDuration = MutableStateFlow(Duration.ZERO)
    val extraLunchDuration: StateFlow<Duration> = _extraLunchDuration.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val twelveHourFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.US)
    private val twentyFourHourFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.US)

    private val standardWorkDuration = Duration.ofHours(8)
    private val standardLunchDuration = Duration.ofMinutes(30)

    val expectedClockOutTime: StateFlow<LocalTime?> = combine(
        clockInTime,
        customClockInTime,
        extraLunchDuration
    ) { clockIn, customClockIn, extraLunch ->
        val effectiveClockIn = customClockIn ?: clockIn
        effectiveClockIn?.plus(standardWorkDuration)?.plus(standardLunchDuration)?.plus(extraLunch)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    init {
        viewModelScope.launch {
            dataStoreManager.getClockInTime().collect { timeString ->
                _clockInTime.value = parseTime(timeString, twentyFourHourFormatter)
            }
        }

        viewModelScope.launch {
            dataStoreManager.getClockOutTime().collect { timeString ->
                _clockOutTime.value = parseTime(timeString, twentyFourHourFormatter)
            }
        }

        viewModelScope.launch {
            dataStoreManager.getCustomClockInTime().collect { timeString ->
                _customClockInTime.value = parseTime(timeString, twentyFourHourFormatter)
            }
        }

        viewModelScope.launch {
            dataStoreManager.getLunchClockInTime().collect { timeString ->
                val lunchInTime = parseTime(timeString, twentyFourHourFormatter)
                _lunchClockInTime.value = lunchInTime
                if (lunchInTime != null && _lunchClockOutTime.value == null) {
                    _expectedLunchBackTime.value = lunchInTime.plusMinutes(30)
                }
            }
        }

        viewModelScope.launch {
            dataStoreManager.getLunchClockOutTime().collect { timeString ->
                val lunchOutTime = parseTime(timeString, twentyFourHourFormatter)
                _lunchClockOutTime.value = lunchOutTime

                if (lunchOutTime != null && lunchClockInTime.value != null) {
                    val lunchIn = lunchClockInTime.value!!
                    val actualLunchDuration = Duration.between(lunchIn, lunchOutTime)
                    val extraDuration = if (actualLunchDuration > standardLunchDuration) {
                        actualLunchDuration.minus(standardLunchDuration)
                    } else {
                        Duration.ZERO
                    }
                    _extraLunchDuration.value = extraDuration
                    Log.d("ClockInViewModel", "Extra Lunch Duration: $extraDuration")
                }

                if (lunchOutTime != null) {
                    _expectedLunchBackTime.value = null
                }
            }
        }
    }

    private fun parseTime(timeString: String?, formatter: DateTimeFormatter): LocalTime? {
        return if (!timeString.isNullOrEmpty()) {
            try {
                LocalTime.parse(timeString, formatter)
            } catch (e: DateTimeParseException) {
                Log.e("ClockInViewModel", "Failed to parse time '$timeString': ${e.message}")
                null
            }
        } else {
            null
        }
    }

    fun clockIn() {
        val currentTime = LocalTime.now()
        _clockInTime.value = currentTime
        _clockOutTime.value = null
        viewModelScope.launch {
            dataStoreManager.saveClockInTime(currentTime.format(twentyFourHourFormatter))
            dataStoreManager.saveClockOutTime(null)
        }
    }

    fun clockOut() {
        val effectiveClockInTime = customClockInTime.value ?: clockInTime.value
        if (effectiveClockInTime == null) {
            Log.e("ClockInViewModel", "Cannot clock out without a clock-in time.")
            return
        }
        val currentTime = LocalTime.now()
        _clockOutTime.value = currentTime
        viewModelScope.launch {
            dataStoreManager.saveClockOutTime(currentTime.format(twentyFourHourFormatter))
        }
    }

    fun updateCustomClockInInput(input: String) {
        _customClockInInput.value = input
        _errorMessage.value = null
    }

    fun saveCustomClockInTime() {
        val input = _customClockInInput.value.trim().toUpperCase(Locale.US)
        if (input.isNotEmpty()) {
            try {
                val parsedTime = LocalTime.parse(input, twelveHourFormatter)
                _customClockInTime.value = parsedTime
                viewModelScope.launch {
                    dataStoreManager.saveCustomClockInTime(parsedTime.format(twentyFourHourFormatter))
                }
                _errorMessage.value = null
            } catch (e: DateTimeParseException) {
                Log.e("ClockInViewModel", "Invalid custom clock-in time format: ${e.message}")
                _errorMessage.value = "Please use the correct format (h:mm AM/PM)."
            }
        } else {
            viewModelScope.launch {
                dataStoreManager.saveCustomClockInTime(null)
            }
            _errorMessage.value = null
        }
    }

    fun lunchClockIn() {
        val currentTime = LocalTime.now()
        _lunchClockInTime.value = currentTime
        _lunchClockOutTime.value = null
        _expectedLunchBackTime.value = currentTime.plusMinutes(30)
        viewModelScope.launch {
            dataStoreManager.saveLunchClockInTime(currentTime.format(twentyFourHourFormatter))
            dataStoreManager.saveLunchClockOutTime(null)
        }
    }

    fun lunchClockOut() {
        val lunchIn = lunchClockInTime.value
        if (lunchIn == null) {
            Log.e("ClockInViewModel", "Cannot clock out from lunch without a lunch clock-in time.")
            return
        }

        val currentTime = LocalTime.now()
        _lunchClockOutTime.value = currentTime

        // Calculate actual lunch duration
        val actualLunchDuration = Duration.between(lunchIn, currentTime)

        // Calculate extra lunch time beyond the standard duration
        val extraDuration = if (actualLunchDuration > standardLunchDuration) {
            actualLunchDuration.minus(standardLunchDuration)
        } else {
            Duration.ZERO
        }
        _extraLunchDuration.value = extraDuration
        _expectedLunchBackTime.value = null

        viewModelScope.launch {
            dataStoreManager.saveLunchClockOutTime(currentTime.format(twentyFourHourFormatter))
        }
    }

    fun resetClockInTime() {
        _clockInTime.value = null
        _customClockInTime.value = null
        _clockOutTime.value = null
        _customClockInInput.value = ""
        _lunchClockInTime.value = null
        _lunchClockOutTime.value = null
        _expectedLunchBackTime.value = null
        _extraLunchDuration.value = Duration.ZERO
        _errorMessage.value = null
        viewModelScope.launch {
            dataStoreManager.saveClockInTime(null)
            dataStoreManager.saveCustomClockInTime(null)
            dataStoreManager.saveClockOutTime(null)
            dataStoreManager.saveLunchClockInTime(null)
            dataStoreManager.saveLunchClockOutTime(null)
        }
    }
}
package com.example.clock

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*

class DataStoreManager(private val dataStore: DataStore<Preferences>) {

    companion object {
        private val CLOCK_IN_TIME_KEY = stringPreferencesKey("clock_in_time")
        private val CLOCK_OUT_TIME_KEY = stringPreferencesKey("clock_out_time")
        private val CUSTOM_CLOCK_IN_TIME_KEY = stringPreferencesKey("custom_clock_in_time")
        private val LUNCH_CLOCK_IN_TIME_KEY = stringPreferencesKey("lunch_clock_in_time")
        private val LUNCH_CLOCK_OUT_TIME_KEY = stringPreferencesKey("lunch_clock_out_time")
    }

    fun getClockInTime(): Flow<String?> = dataStore.data.map { preferences ->
        preferences[CLOCK_IN_TIME_KEY]
    }

    suspend fun saveClockInTime(time: String?) {
        dataStore.edit { preferences ->
            if (time != null) {
                preferences[CLOCK_IN_TIME_KEY] = time
            } else {
                preferences.remove(CLOCK_IN_TIME_KEY)
            }
        }
    }

    fun getClockOutTime(): Flow<String?> = dataStore.data.map { preferences ->
        preferences[CLOCK_OUT_TIME_KEY]
    }

    suspend fun saveClockOutTime(time: String?) {
        dataStore.edit { preferences ->
            if (time != null) {
                preferences[CLOCK_OUT_TIME_KEY] = time
            } else {
                preferences.remove(CLOCK_OUT_TIME_KEY)
            }
        }
    }

    fun getCustomClockInTime(): Flow<String?> = dataStore.data.map { preferences ->
        preferences[CUSTOM_CLOCK_IN_TIME_KEY]
    }

    suspend fun saveCustomClockInTime(time: String?) {
        dataStore.edit { preferences ->
            if (time != null) {
                preferences[CUSTOM_CLOCK_IN_TIME_KEY] = time
            } else {
                preferences.remove(CUSTOM_CLOCK_IN_TIME_KEY)
            }
        }
    }

    // Methods for Lunch Clock-In and Clock-Out
    fun getLunchClockInTime(): Flow<String?> = dataStore.data.map { preferences ->
        preferences[LUNCH_CLOCK_IN_TIME_KEY]
    }

    suspend fun saveLunchClockInTime(time: String?) {
        dataStore.edit { preferences ->
            if (time != null) {
                preferences[LUNCH_CLOCK_IN_TIME_KEY] = time
            } else {
                preferences.remove(LUNCH_CLOCK_IN_TIME_KEY)
            }
        }
    }

    fun getLunchClockOutTime(): Flow<String?> = dataStore.data.map { preferences ->
        preferences[LUNCH_CLOCK_OUT_TIME_KEY]
    }

    suspend fun saveLunchClockOutTime(time: String?) {
        dataStore.edit { preferences ->
            if (time != null) {
                preferences[LUNCH_CLOCK_OUT_TIME_KEY] = time
            } else {
                preferences.remove(LUNCH_CLOCK_OUT_TIME_KEY)
            }
        }
    }
}
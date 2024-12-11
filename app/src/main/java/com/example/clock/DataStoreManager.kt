package com.example.clock

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreManager(private val context: Context) {
    private val Context.dataStore by preferencesDataStore(name = "clock_prefs")

    companion object {
        val CLOCK_IN_TIME_KEY = stringPreferencesKey("clock_in_time")
        val CLOCK_OUT_TIME_KEY = stringPreferencesKey("clock_out_time")
        val CUSTOM_CLOCK_IN_TIME_KEY = stringPreferencesKey("custom_clock_in_time")
    }

    fun getClockInTime(): Flow<String?> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[CLOCK_IN_TIME_KEY]
            }
    }

    fun getClockOutTime(): Flow<String?> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[CLOCK_OUT_TIME_KEY]
            }
    }

    fun getCustomClockInTime(): Flow<String?> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[CUSTOM_CLOCK_IN_TIME_KEY]
            }
    }

    suspend fun saveClockInTime(time: String?) {
        context.dataStore.edit { preferences ->
            if (!time.isNullOrEmpty()) {
                preferences[CLOCK_IN_TIME_KEY] = time
            } else {
                preferences.remove(CLOCK_IN_TIME_KEY)
            }
        }
    }

    suspend fun saveClockOutTime(time: String?) {
        context.dataStore.edit { preferences ->
            if (time != null) {
                preferences[CLOCK_OUT_TIME_KEY] = time
            } else {
                preferences.remove(CLOCK_OUT_TIME_KEY)
            }
        }
    }

    suspend fun saveCustomClockInTime(time: String?) {
        context.dataStore.edit { preferences ->
            if (!time.isNullOrEmpty()) {
                preferences[CUSTOM_CLOCK_IN_TIME_KEY] = time
            } else {
                preferences.remove(CUSTOM_CLOCK_IN_TIME_KEY)
            }
        }
    }
}
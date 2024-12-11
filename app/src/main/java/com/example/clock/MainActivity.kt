package com.example.clock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clock.ui.theme.ClockInTheme

// Extension property to create DataStore
val ComponentActivity.dataStore by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClockInTheme {
                val dataStoreManager = DataStoreManager(dataStore)
                val viewModel: ClockInViewModel = viewModel(factory = ClockInViewModelFactory(dataStoreManager))
                ClockInApp(viewModel = viewModel)
            }
        }
    }
}
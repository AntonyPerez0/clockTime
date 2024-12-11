package com.example.clock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.clock.ui.theme.ClockInAppTheme

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: ClockInViewModel
    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataStoreManager = DataStoreManager(this)
        viewModel = ViewModelProvider(this,
            ClockInViewModelFactory(dataStoreManager)
        ).get(ClockInViewModel::class.java)

        setContent {
            ClockInAppTheme {
                ClockInApp(viewModel = viewModel)
            }
        }
    }
}
package com.example.clock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ClockInViewModelFactory(private val dataStoreManager: DataStoreManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ClockInViewModel(dataStoreManager) as T
    }
}
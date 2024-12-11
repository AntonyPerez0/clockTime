package com.example.clock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ClockInViewModelFactory(private val dataStoreManager: DataStoreManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClockInViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ClockInViewModel(dataStoreManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.example.wearit.ui

import androidx.lifecycle.ViewModel
import com.example.wearit.model.AppUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState = _uiState.asStateFlow()
}
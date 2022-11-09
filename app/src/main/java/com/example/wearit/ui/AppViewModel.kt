package com.example.wearit.ui

import androidx.lifecycle.ViewModel
import com.example.wearit.model.AppUiState
import com.example.wearit.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState = _uiState.asStateFlow()

    fun goToCategory(category: Category){
        _uiState.value = AppUiState(category)
    }

}
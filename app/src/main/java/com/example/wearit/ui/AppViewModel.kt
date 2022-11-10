package com.example.wearit.ui

import androidx.lifecycle.ViewModel
import com.example.wearit.model.AppUiState
import com.example.wearit.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState = _uiState.asStateFlow()

    fun goToCategory(category: Category){
        _uiState.update { currentState ->
            currentState.copy(
                currentCategory = category
            )
        }
    }
}
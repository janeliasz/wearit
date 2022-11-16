package com.example.wearit.ui

import androidx.lifecycle.ViewModel
import com.example.wearit.model.AppUiState
import com.example.wearit.model.Category
import com.example.wearit.model.Item
import com.example.wearit.model.fakeItemsData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState = _uiState.asStateFlow()

    val currentSelection: List<Item> = listOf(
        fakeItemsData.getValue(Category.Headgear)[0],
        fakeItemsData.getValue(Category.Blouse)[0],
        fakeItemsData.getValue(Category.Tshirt)[0],
        fakeItemsData.getValue(Category.Trousers)[0],
        fakeItemsData.getValue(Category.Boots)[0],
    )

    fun goToCategory(category: Category){
        _uiState.update { currentState ->
            currentState.copy(
                currentCategory = category
            )
        }
    }

    fun changeItem(category: Category, next: Boolean) {

    }
}
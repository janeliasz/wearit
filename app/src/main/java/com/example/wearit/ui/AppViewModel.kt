package com.example.wearit.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.wearit.model.AppUiState
import com.example.wearit.model.Category
import com.example.wearit.model.Item
import com.example.wearit.model.fakeItemsData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.lang.Integer.max

val initialCurrentSelection: MutableList<Item> = mutableListOf(
    fakeItemsData.getValue(Category.Headgear)[0],
    fakeItemsData.getValue(Category.Blouse)[0],
    fakeItemsData.getValue(Category.Tshirt)[0],
    fakeItemsData.getValue(Category.Trousers)[0],
    fakeItemsData.getValue(Category.Boots)[0]
)

class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState(currentSelection = initialCurrentSelection))
    val uiState = _uiState.asStateFlow()

    fun goToCategory(category: Category) {
        _uiState.update { currentState ->
            currentState.copy(
                currentCategory = category
            )
        }
    }

    fun changeSelectedItem(category: Category, next: Boolean) {
        val newCurrentSelection = _uiState.value.currentSelection.map { item ->
            if (item.category != category)
                item
            else {
                val allItemsInCurrentItemCategory = fakeItemsData.getValue(category)
                val currentItemIndex = allItemsInCurrentItemCategory.indexOf(item)
                var newCurrentItemIndex = currentItemIndex + if (next) 1 else -1

                if (newCurrentItemIndex == allItemsInCurrentItemCategory.size)
                    newCurrentItemIndex = 0
                else if (newCurrentItemIndex < 0)
                    newCurrentItemIndex = allItemsInCurrentItemCategory.size - 1

                allItemsInCurrentItemCategory[newCurrentItemIndex]
            }
        }

        _uiState.update { currentState ->
            currentState.copy(
                currentSelection = newCurrentSelection
            )
        }
    }
}
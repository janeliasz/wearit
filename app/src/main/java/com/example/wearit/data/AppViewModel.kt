package com.example.wearit.data

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.wearit.model.AppUiState
import com.example.wearit.model.Category
import com.example.wearit.model.Item
import com.example.wearit.model.fakeItemsData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

val initialCurrentSelection: MutableList<Item> = mutableListOf(
    fakeItemsData.getValue(Category.Headgear)[0],
    fakeItemsData.getValue(Category.Blouse)[0],
    fakeItemsData.getValue(Category.Tshirt)[0],
    fakeItemsData.getValue(Category.Trousers)[0],
    fakeItemsData.getValue(Category.Boots)[0]
)

class AppViewModel(context: Context) : ViewModel() {
    private val internalStorageHelper = InternalStorageHelper()

    private val _uiState = MutableStateFlow(AppUiState(
        items = internalStorageHelper.getItemsMap(context),
        currentSelection = initialCurrentSelection
    ))
    val uiState = _uiState.asStateFlow()

    fun addItem(context: Context, name: String, bitmap: Bitmap): Boolean {
        val photoFilename = internalStorageHelper.savePhoto(context, bitmap)
        if (photoFilename == "") return false;

        val newItem = Item(
            id = UUID.randomUUID().toString(),
            name = name,
            photoFilename = photoFilename,
            category = _uiState.value.currentCategory
        )

        val saved = internalStorageHelper.saveItem(context, newItem)

        if (saved) {
            val newItems = _uiState.value.items.toMutableMap()
            if (newItems.containsKey(newItem.category)) {
                val mutableList = newItems.getValue(newItem.category).toMutableList()
                mutableList.add(newItem)
                newItems[newItem.category] = mutableList
            }
            else {
                newItems[newItem.category] = listOf(newItem)
            }

            _uiState.update { currentState ->
                currentState.copy(
                    items = newItems
                )
            }
        }

        return saved
    }

    fun goToCategory(category: Category){
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
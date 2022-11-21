package com.example.wearit.data

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.wearit.model.AppUiState
import com.example.wearit.model.Category
import com.example.wearit.model.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import kotlin.random.Random

fun getInitialCurrentSelection(items: Map<Category, List<Item>>): List<String> {
    val selection = mutableListOf<String>()

    items.forEach { entry ->
        val activeItems = entry.value.filter { item -> item.isActive }
        if (activeItems.isNotEmpty()) {
            selection.add(activeItems[Random.nextInt(activeItems.size)].id)
        }
    }

    return selection
}

class AppViewModel(context: Context) : ViewModel() {
    private val internalStorageHelper = InternalStorageHelper(context)
    private val loadedItemsMap = internalStorageHelper.getItemsMap()
    private val loadedPhotosMap = internalStorageHelper.loadPhotos()

    private val _uiState = MutableStateFlow(AppUiState(
        items = loadedItemsMap,
        currentSelection = getInitialCurrentSelection(loadedItemsMap)
    ))
    val uiState = _uiState.asStateFlow()

    fun getItemById(id: String): Item? {
        _uiState.value.items.forEach { entry ->
            entry.value.forEach { item ->
                if (item.id == id) return item
            }
        }
        return null
    }

    fun getItemPhotoByPhotoFilename(id: String): Bitmap? {
        return try {
            loadedPhotosMap.entries.first { entry ->
                entry.key.startsWith(id)
            }.value
        } catch (e: NoSuchElementException) {
            e.printStackTrace()
            null
        }
    }

    fun goToCategory(category: Category) {
        _uiState.update { currentState ->
            currentState.copy(
                currentCategory = category
            )
        }
    }

    fun changeSelectedItem(category: Category, next: Boolean) {
        val newCurrentSelection = _uiState.value.currentSelection.map { itemId ->
            val item = getItemById(itemId)!!

            if (item.category != category)
                itemId
            else {
                val allItemsInCurrentItemCategory = _uiState.value.items.getValue(category)
                val currentItemIndex = allItemsInCurrentItemCategory.indexOf(item)
                var newCurrentItemIndex = currentItemIndex + if (next) 1 else -1

                if (newCurrentItemIndex == allItemsInCurrentItemCategory.size)
                    newCurrentItemIndex = 0
                else if (newCurrentItemIndex < 0)
                    newCurrentItemIndex = allItemsInCurrentItemCategory.size - 1

                allItemsInCurrentItemCategory[newCurrentItemIndex].id
            }
        }

        _uiState.update { currentState ->
            currentState.copy(
                currentSelection = newCurrentSelection
            )
        }
    }

    fun addItem(name: String, bitmap: Bitmap): Boolean {
        val photoFilename = internalStorageHelper.savePhoto(bitmap)
        if (photoFilename == "") return false

        val newItem = Item(
            id = UUID.randomUUID().toString(),
            name = name,
            photoFilename = photoFilename,
            category = _uiState.value.currentCategory
        )

        val saved = internalStorageHelper.saveItem(newItem)

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
}
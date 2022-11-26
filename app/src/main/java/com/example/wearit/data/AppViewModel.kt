package com.example.wearit.data

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.*
import com.example.wearit.model.AppUiState
import com.example.wearit.model.Category
import com.example.wearit.model.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val internalStorageHelper = InternalStorageHelper(application.applicationContext)
    private val loadedPhotos = internalStorageHelper.loadPhotos().toMutableMap()

    private val repository: AppRepository
    val getAllItems: StateFlow<List<Item>>

    init {
        val itemDao = AppDatabase.getInstance(application).itemDao()
        repository = AppRepository(itemDao)
        getAllItems = repository.getAllItems.stateIn(
            initialValue = listOf(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )
    }

    private val _uiState = MutableStateFlow(
        AppUiState(
            currentSelection = listOf()
        )
    )
    val uiState = _uiState.asStateFlow()

    fun getItemById(id: Int): Item? {
        val itemList: List<Item> = getAllItems.value
        return itemList.find { item -> item.id == id }
    }

    fun getItemPhotoByPhotoFilename(filename: String): Bitmap? {
        return loadedPhotos.entries.find { entry ->
            entry.key.startsWith(filename)
        }?.value
    }

    fun changeSelectedItem(category: Category, next: Boolean) {
        val itemList: List<Item> = getAllItems.value
        val itemMap: Map<Category, List<Item>> = getItemMap(itemList)

        val newCurrentSelection = _uiState.value.currentSelection.map { itemId ->
            val item = getItemById(itemId) ?: return

            if (item.category != category)
                itemId
            else {
                val allItemsInCurrentItemCategory = itemMap.getValue(category)
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

    fun drawItems() {
        val itemList: List<Item> = getAllItems.value
        val itemMap: Map<Category, List<Item>> = getItemMap(itemList)

        val newCurrentSelection = mutableListOf<Int>()

        itemMap.forEach { entry ->
            val listOfActiveItems = entry.value.filter { it.isActive }
            if (listOfActiveItems.isNotEmpty()) {
                newCurrentSelection.add(listOfActiveItems.random().id)
            }
        }

        _uiState.update { currentState ->
            currentState.copy(
                currentSelection = newCurrentSelection
            )
        }
    }

    fun saveItem(name: String, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            val photoFilename = internalStorageHelper.savePhoto(bitmap)
            if (photoFilename != "") {
                val newItem = Item(
                    id = 0,
                    name = name,
                    photoFilename = photoFilename,
                    category = _uiState.value.currentCategory
                )

                loadedPhotos[photoFilename] = bitmap

                repository.addItem(item = newItem)
            }
        }
    }

    fun updateItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateItem(item)
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            internalStorageHelper.deleteFile(item.photoFilename)
            repository.deleteItem(item)
            _uiState.update { currentState ->
                currentState.copy(
                    currentSelection = _uiState.value.currentSelection.filter { id -> id != item.id }
                )
            }
        }
    }

    fun goToCategory(category: Category) {
        _uiState.update { currentState ->
            currentState.copy(
                currentCategory = category
            )
        }
    }
}

fun getInitialCurrentSelection(items: Map<Category, List<Item>>): List<Int> {
    val selection = mutableListOf<Int>()

    items.forEach { entry ->
        val activeItems = entry.value.filter { item -> item.isActive }
        if (activeItems.isNotEmpty()) {
            selection.add(activeItems[Random.nextInt(activeItems.size)].id)
        }
    }

    return selection
}

fun getItemMap(itemList: List<Item>): Map<Category, List<Item>> {
    val itemsMap = mutableMapOf<Category, MutableList<Item>>()

    itemList.forEach { item ->
        if (itemsMap.containsKey(item.category)) {
            itemsMap.getValue(item.category).add(item)
        }
        else {
            itemsMap[item.category] = mutableListOf(item)
        }
    }

    return itemsMap
}
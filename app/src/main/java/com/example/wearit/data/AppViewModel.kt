package com.example.wearit.data

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.*
import com.example.wearit.model.AppUiState
import com.example.wearit.model.Category
import com.example.wearit.model.Item
import com.example.wearit.model.Outfit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val internalStorageHelper = InternalStorageHelper(application.applicationContext)
    private val loadedPhotos = internalStorageHelper.loadPhotos().toMutableMap()

    private val repository: AppRepository
    val getAllItems: StateFlow<List<Item>>
    val getAllOutfits: StateFlow<List<Outfit>>

    private val dataStore: StoreSettings
    val getIsAppInDarkTheme: StateFlow<Boolean>

    init {
        val itemDao = AppDatabase.getInstance(application).itemDao()
        val outfitDao = AppDatabase.getInstance(application).outfitDao()

        repository = AppRepository(itemDao, outfitDao)

        getAllItems = repository.getAllItems.stateIn(
            initialValue = listOf(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

        getAllOutfits = repository.getAllOutfits.stateIn(
            initialValue = listOf(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

        dataStore = StoreSettings(application.applicationContext)

        getIsAppInDarkTheme = dataStore.getIsAppInDarkTheme.stateIn(
            initialValue = runBlocking { dataStore.getIsAppInDarkTheme.first() },
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

        val newCurrentSelection = _uiState.value.currentSelection.mapNotNull { itemId ->
            val item = getItemById(itemId) ?: return@mapNotNull null

            if (item.category != category)
                itemId
            else {
                val activeItemListWithCurrentItem = itemList
                    .filter { (it.category == category && it.isActive) || it.id == itemId}

                val currentItemIndex = activeItemListWithCurrentItem.indexOf(item)
                var newCurrentItemIndex = currentItemIndex + if (next) 1 else -1

                if (newCurrentItemIndex == activeItemListWithCurrentItem.size)
                    newCurrentItemIndex = 0
                else if (newCurrentItemIndex < 0)
                    newCurrentItemIndex = activeItemListWithCurrentItem.size - 1

                if (activeItemListWithCurrentItem[newCurrentItemIndex].isActive)
                    activeItemListWithCurrentItem[newCurrentItemIndex].id
                else
                    null
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
        val itemMap: Map<Category, List<Item>> = getItemMap(itemList.sortedBy { it.category })

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

    fun setItemActiveInactive(item: Item) {
        updateItem(item.copy(isActive = !item.isActive))
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


    fun saveOutfit() {
        viewModelScope.launch(Dispatchers.IO) {
            if (!getAllOutfits.value.any { it.itemsInOutfit.sorted() == _uiState.value.currentSelection.sorted() }
                && _uiState.value.currentSelection.isNotEmpty()) {
                val newOutfit = Outfit(
                    id = 0,
                    itemsInOutfit = _uiState.value.currentSelection
                )
                repository.addOutfit(outfit = newOutfit)
            }
        }
    }

    fun switchTheme(darkMode: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.saveIsAppInDarkTheme(darkMode)
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
        } else {
            itemsMap[item.category] = mutableListOf(item)
        }
    }

    return itemsMap
}


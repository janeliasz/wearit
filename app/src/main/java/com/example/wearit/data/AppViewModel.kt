package com.example.wearit.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wearit.model.Category
import com.example.wearit.model.Item
import com.example.wearit.model.Outfit
import dagger.hilt.android.lifecycle.HiltViewModel
import getDrawnItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import getNextItem
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val appRepository: IAppRepository,
    private val storeSettings: IStoreSettings,
    private val internalStorageHelper: IInternalStorageHelper
) : ViewModel() {
    private var loadedPhotos = internalStorageHelper.loadPhotos().toMutableMap()

    val getAllItems: StateFlow<List<Item>> = appRepository.getAllItems.stateIn(
        initialValue = runBlocking { appRepository.getAllItems.first() },
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )
    val getAllOutfits: StateFlow<List<Outfit>> = appRepository.getAllOutfits.stateIn(
        initialValue = runBlocking { appRepository.getAllOutfits.first() },
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    val getIsAppInDarkTheme: StateFlow<Boolean> = storeSettings.getIsAppInDarkTheme.stateIn(
        initialValue = runBlocking { storeSettings.getIsAppInDarkTheme.first() },
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    private val _uiState = MutableStateFlow(
        com.example.wearit.di.AppUiState(
            currentSelection = listOf()
        )
    )
    val uiState = _uiState.asStateFlow()

    fun getItemById(id: Int): Item? {
        val itemList: List<Item> = getAllItems.value
        return itemList.find { item -> item.id == id }
    }

    fun getItemPhotoByPhotoFilename(filename: String): Bitmap? {
        return try{
            val photoByteArray = loadedPhotos.entries.find { entry ->
                entry.key.startsWith(filename)
            }?.value
            if (photoByteArray != null) {
                BitmapFactory.decodeByteArray(photoByteArray, 0, photoByteArray.size)
            } else {
                null
            }
        }catch (_:java.lang.NullPointerException){

            null
        }
    }

    fun changeSelectedItem(category: Category, next: Boolean) {
        val itemList: List<Item> = getAllItems.value

        val newCurrentSelection = _uiState.value.currentSelection.mapNotNull { itemId ->
            val item = getItemById(itemId) ?: return@mapNotNull null

            if (item.category != category)
                itemId
            else {
                getNextItem(itemList.filter { it.category == category }, itemId, next)
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
        val newCurrentSelection = getDrawnItems(itemList)

        _uiState.update { currentState ->
            currentState.copy(
                currentSelection = newCurrentSelection
            )
        }
    }

    fun saveItem(name: String, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentCategory = _uiState.value.currentCategory
            val photoFilename = internalStorageHelper.savePhoto(bitmap)
            if (photoFilename != "") {
                val newItem = Item(
                    id = 0,
                    name = name,
                    photoFilename = photoFilename,
                    category = currentCategory
                )

                loadedPhotos = internalStorageHelper.loadPhotos().toMutableMap()


                appRepository.addItem(item = newItem)
            }
        }
    }

    fun setItemActiveInactive(item: Item) {
        updateItem(item.copy(isActive = !item.isActive))
    }

    fun updateItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.updateItem(item)
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { currentState ->
                currentState.copy(
                    currentSelection = _uiState.value.currentSelection.filter { id -> id != item.id }
                )
            }
            getAllOutfits.value.forEach { outfit ->
                if (outfit.itemsInOutfit.contains(item.id)) {
                    updateOutfit(outfit.copy(itemsInOutfit = outfit.itemsInOutfit.filter { itemId -> itemId != item.id }))
                }
            }
            internalStorageHelper.deleteFile(item.photoFilename)
            appRepository.deleteItem(item)
        }
    }

    fun goToCategory(category: Category) {
        _uiState.update { currentState ->
            currentState.copy(
                currentCategory = category
            )
        }
    }

    fun saveOutfit(): Boolean? {
        if (_uiState.value.currentSelection.isEmpty()) return null

        var flag = false

        if (!getAllOutfits.value.any { it.itemsInOutfit.sorted() == _uiState.value.currentSelection.sorted() }) {

            val newOutfit = Outfit(
                id = 0,
                itemsInOutfit = _uiState.value.currentSelection
            )
            viewModelScope.launch(Dispatchers.IO) {
                appRepository.addOutfit(outfit = newOutfit)
            }

            flag = true
        }



        return flag
    }

    fun updateOutfit(outfit: Outfit) {
        viewModelScope.launch(Dispatchers.IO) {
            if (outfit.itemsInOutfit.isEmpty()) {
                deleteOutfit(outfit)
            } else {
                appRepository.updateOutfit(outfit)
            }
        }
    }

    fun deleteOutfit(outfit: Outfit) {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.deleteOutfit(outfit)
        }
    }

    fun switchTheme(darkMode: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            storeSettings.saveIsAppInDarkTheme(darkMode)
        }
    }

}

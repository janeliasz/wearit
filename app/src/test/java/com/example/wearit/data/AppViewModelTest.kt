package com.example.wearit.data

import com.example.wearit.model.Category
import org.junit.Before
import org.junit.Test

class AppViewModelTest {
    private lateinit var appViewModel: AppViewModel
    private lateinit var appRepository: IAppRepository
    private lateinit var storeSettings: IStoreSettings
    private lateinit var internalStorageHelper: IInternalStorageHelper

    @Before
    fun setUp() {
        appRepository = FakeAppRepository(fakeItems, fakeOutfits)
        storeSettings = FakeStoreSettings()
        internalStorageHelper = FakeInternalStorageHelper()

        appViewModel = AppViewModel(
            appRepository,
            storeSettings,
            internalStorageHelper
        )
    }

    @Test
    fun `get item by id`() {
        val result = appViewModel.getItemById(fakeItems[0].id)

        val expectedItem = fakeItems[0]

        assert(result == expectedItem)
    }

    @Test
    fun `return null if no item with id`() {
        val result = appViewModel.getItemById(0)

        val expectedItem = null

        assert(result == expectedItem)
    }

    @Test
    fun `change selected item`() {
        appViewModel.drawItems()

        val currentItem = appViewModel.uiState.value.currentSelection[0]

        appViewModel.changeSelectedItem(category = Category.Headgear, next = true)

        val newCurrentItem = appViewModel.uiState.value.currentSelection[0]

        assert(newCurrentItem != currentItem)
    }

    @Test
    fun `draw items`() {
        appViewModel.drawItems()

        assert(appViewModel.uiState.value.currentSelection.isNotEmpty())
    }

    @Test
    fun `change current category`() {
        appViewModel.goToCategory(Category.Tshirt)

        assert(appViewModel.uiState.value.currentCategory == Category.Tshirt)
    }
}
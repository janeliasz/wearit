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
        appRepository = FakeAppRepository()
        storeSettings = FakeStoreSettings()
        internalStorageHelper = FakeInternalStorageHelper()

        appViewModel = AppViewModel(
            appRepository,
            storeSettings,
            internalStorageHelper
        )
    }

    @Test
    fun `should change current category`() {
        appViewModel.goToCategory(Category.Tshirt)

        assert(appViewModel.uiState.value.currentCategory == Category.Tshirt)
    }
}
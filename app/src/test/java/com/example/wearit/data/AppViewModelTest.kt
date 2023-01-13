package com.example.wearit.data

import org.junit.Assert
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
    fun `dummy test which should pass`() {
        Assert.assertEquals(4, 2 + 2)
    }
}
package com.example.wearit.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeStoreSettings : IStoreSettings {
    var isAppInDarkTheme = false

    override val getIsAppInDarkTheme: Flow<Boolean> = flow { emit(isAppInDarkTheme) }

    override suspend fun saveIsAppInDarkTheme(value: Boolean) {
        isAppInDarkTheme = value
    }
}
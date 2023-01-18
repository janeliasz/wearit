package com.example.wearit.data

import kotlinx.coroutines.flow.Flow

interface IStoreSettings {
    val getIsAppInDarkTheme: Flow<Boolean>

    suspend fun saveIsAppInDarkTheme(value: Boolean)
}
package com.example.wearit.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreSettings(private val context: Context) : IStoreSettings {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
        val IS_APP_IN_DARK_THEME_KEY = booleanPreferencesKey("isAppInDarkTheme")
    }

    override val getIsAppInDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_APP_IN_DARK_THEME_KEY] ?: false
        }

    override suspend fun saveIsAppInDarkTheme(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_APP_IN_DARK_THEME_KEY] = value
        }
    }
}
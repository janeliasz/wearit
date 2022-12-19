package com.example.wearit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.example.wearit.data.StoreSettings
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private val DarkColorPalette = darkColors(
    primary = MainBlack,
    onPrimary = White,
    surface = BlackVariantLight,
    onSurface = White,
    background = BlackVariantDark,
    onBackground = White
)

private val LightColorPalette = lightColors(
    primary = MainRed,
    onPrimary = White,
    surface = RedVariant,
    onSurface = White,
    background = White,
    onBackground = MainRed
)

@Composable
fun WearItTheme(content: @Composable () -> Unit) {
    val dataStore = StoreSettings(LocalContext.current)
    val isAppInDarkTheme = dataStore.getIsAppInDarkTheme.collectAsState(initial = runBlocking { dataStore.getIsAppInDarkTheme.first() })

    val colors = if (isAppInDarkTheme.value) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
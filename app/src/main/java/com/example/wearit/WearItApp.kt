package com.example.wearit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wearit.ui.AppViewModel
import com.example.wearit.ui.PickerScreen
import com.example.wearit.ui.WardrobeScreen

enum class WearItScreen() {
    Picker,
    Wardrobe
}

@Preview
@Composable
fun WearItApp() {
    val viewModel: AppViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = WearItScreen.Picker.name
    ) {
        composable(WearItScreen.Picker.name) {
            PickerScreen(onButtonClick = { navController.navigate(WearItScreen.Wardrobe.name) })
        }

        composable(WearItScreen.Wardrobe.name) {
            WardrobeScreen(currentCategory = uiState.currentCategory)
        }
    }
}
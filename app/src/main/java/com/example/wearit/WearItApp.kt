package com.example.wearit

import IntroScreen
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wearit.model.fakeItemsData
import com.example.wearit.ui.AppViewModel
import com.example.wearit.ui.PickerScreen
import com.example.wearit.ui.WardrobeScreen

enum class WearItScreen() {
    Intro,
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
        startDestination = WearItScreen.Intro.name
    ) {
        composable(WearItScreen.Intro.name){
            IntroScreen(navigateToPicker = {navController.navigate(WearItScreen.Picker.name)})
        }
        composable(WearItScreen.Picker.name) {
            PickerScreen(
                onButtonClick = { navController.navigate(WearItScreen.Wardrobe.name) },
                currentSelection = uiState.currentSelection,
                changeSelectedItem = { category, next ->  viewModel.changeSelectedItem(category, next) }
            )
        }

        composable(WearItScreen.Wardrobe.name) {
            WardrobeScreen(
                currentCategory = uiState.currentCategory,
                onCategoryChange = { viewModel.goToCategory(it) },
                //picker screen:
                goToPickerScreen = { navController.navigate(WearItScreen.Picker.name) },
                itemsOfCurrentCategory = fakeItemsData[uiState.currentCategory]
            )
        }
    }
}
//
package com.example.wearit

import IntroScreen

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import android.graphics.BitmapFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.example.wearit.data.AppViewModel
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
    val context = LocalContext.current

    val viewModel = AppViewModel(LocalContext.current)
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
                getItemPhotoByPhotoFilename = { itemId ->  viewModel.getItemPhotoByPhotoFilename(itemId)!! },
                currentSelection = uiState.currentSelection.map { itemId -> viewModel.getItemById(itemId)!! },
                changeSelectedItem = { category, next ->  viewModel.changeSelectedItem(category, next) }
            )
        }

        composable(WearItScreen.Wardrobe.name) {
            WardrobeScreen(
                onCategoryChange = { viewModel.goToCategory(it) },
                goToPickerScreen = { navController.navigate(WearItScreen.Picker.name) },
//                itemsOfCurrentCategory = fakeItemsData[uiState.currentCategory]!!,
                itemsOfCurrentCategory = uiState.items[uiState.currentCategory]!!,
                addItem = { bitmap -> viewModel.addItem(context, "test", bitmap)},
            getItemPhotoByPhotoFilename = { itemId ->  viewModel.getItemPhotoByPhotoFilename(itemId)!! },
            )
        }
    }
}
//
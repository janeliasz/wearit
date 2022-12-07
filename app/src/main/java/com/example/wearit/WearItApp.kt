package com.example.wearit

import IntroScreen
import android.app.Application
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.example.wearit.data.AppViewModel
import com.example.wearit.ui.FavoritesScreen
import com.example.wearit.ui.PickerScreen
import com.example.wearit.ui.WardrobeScreen

enum class WearItScreen() {
    Intro,
    Picker,
    Wardrobe,
    Favorites,
}

@Preview
@Composable
fun WearItApp() {
    val viewModel = AppViewModel(LocalContext.current.applicationContext as Application)
    val uiState by viewModel.uiState.collectAsState()
    val items by viewModel.getAllItems.collectAsState()
    val outfits by viewModel.getAllOutfits.collectAsState()

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = WearItScreen.Intro.name
    ) {
        composable(WearItScreen.Intro.name) {
            IntroScreen(navigateToPicker = { navController.navigate(WearItScreen.Picker.name) })
        }
        composable(WearItScreen.Picker.name) {
            PickerScreen(
                goToWardrobe = { navController.navigate(WearItScreen.Wardrobe.name) },
                getItemPhotoByPhotoFilename = { itemId ->
                    viewModel.getItemPhotoByPhotoFilename(
                        itemId
                    )!!
                },
                currentSelection = uiState.currentSelection.mapNotNull { itemId ->
                    viewModel.getItemById(
                        itemId
                    )
                },
                changeSelectedItem = { category, next ->
                    viewModel.changeSelectedItem(
                        category,
                        next
                    )
                },
                drawSelection = {viewModel.drawItems()},
                saveOutfit = {viewModel.saveOutfit()}

            )
        }

        composable(WearItScreen.Wardrobe.name) {
            WardrobeScreen(
                onCategoryChange = { viewModel.goToCategory(it) },
                goToPickerScreen = { navController.navigate(WearItScreen.Picker.name) },
                itemsOfCurrentCategory = items.filter { item -> item.category == uiState.currentCategory },
                saveItem = { bitmap -> viewModel.saveItem("test", bitmap) },
                getItemPhotoByPhotoFilename = { itemId ->
                    viewModel.getItemPhotoByPhotoFilename(
                        itemId
                    )!!
                },
                setActiveInactive = { viewModel.setItemActiveInactive(it) },
                currentCategory = uiState.currentCategory,
                goToFavorites = { navController.navigate(WearItScreen.Favorites.name)}
            )

        }

        composable(WearItScreen.Favorites.name){
            FavoritesScreen(
                goToPickerScreen = { navController.navigate(WearItScreen.Picker.name) },
                goToWardrobe = { navController.navigate(WearItScreen.Wardrobe.name) },
            )
        }

    }
}

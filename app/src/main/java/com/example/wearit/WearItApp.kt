package com.example.wearit

import IntroScreen
import ItemInfo
import SettingsScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.wearit.model.AppViewModel
import com.example.wearit.ui.FavoritesScreen
import com.example.wearit.ui.PickerScreen
import com.example.wearit.ui.WardrobeScreen

enum class WearItScreen() {
    Intro,
    Picker,
    Wardrobe,
    ItemInfo,
    Favorites,
    Settings
}

@Composable
fun WearItApp() {
    val viewModel: AppViewModel = hiltViewModel<AppViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val items by viewModel.getAllItems.collectAsState()
    val outfits by viewModel.getAllOutfits.collectAsState()
    val isAppInDarkTheme by viewModel.getIsAppInDarkTheme.collectAsState()

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
                    )
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
                drawSelection = { viewModel.drawItems() },
                saveOutfit = { viewModel.saveOutfit() },
                goToSettings = { navController.navigate(WearItScreen.Settings.name) }
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
                goToSingleItem = { itemId -> navController.navigate(WearItScreen.ItemInfo.name+"/${itemId}")},
                goToFavorites = { navController.navigate(WearItScreen.Favorites.name)},
                deleteItem = { viewModel.deleteItem(it) }
            )
        }
        composable(
            route = "${WearItScreen.ItemInfo.name}/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) {
            backStackEntry ->
            ItemInfo(
                returnToWardrobe = { navController.navigate(WearItScreen.Wardrobe.name) },
                itemId = backStackEntry.arguments?.getString("itemId")!!,
                getItemById = { itemId -> viewModel.getItemById(itemId.toInt())!! },
                deleteItem = { viewModel.deleteItem(it) },
                getItemPhotoByPhotoFilename = { itemId ->
                    viewModel.getItemPhotoByPhotoFilename(
                        itemId
                    )!!
                },
            )
        }
        composable(WearItScreen.Settings.name) {
            SettingsScreen(
                isAppInDarkTheme = isAppInDarkTheme,
                switchTheme = { darkMode -> viewModel.switchTheme(darkMode) },
                goToPicker = { navController.navigate(WearItScreen.Picker.name) }
            )
        }

        composable(WearItScreen.Favorites.name){
            FavoritesScreen(
                goToPickerScreen = { navController.navigate(WearItScreen.Picker.name) },
                goToWardrobe = { navController.navigate(WearItScreen.Wardrobe.name) },
                outfits = outfits,
                getItemById = {viewModel.getItemById(it)  },
                deleteOutfit={viewModel.deleteOutfit(it)},
                getItemPhotoByPhotoFilename = { itemId ->
                    viewModel.getItemPhotoByPhotoFilename(
                        itemId
                    )!!
                }
            )
        }
    }
}

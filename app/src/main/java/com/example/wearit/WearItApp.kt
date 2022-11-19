package com.example.wearit

import IntroScreen
import android.media.Image
import android.view.animation.OvershootInterpolator
import android.window.SplashScreen
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wearit.data.AppViewModel
import com.example.wearit.ui.PickerScreen
import com.example.wearit.ui.WardrobeScreen


import kotlinx.coroutines.delay

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
                addItem = { bitmap -> viewModel.addItem(context, "test", bitmap)}
            )
        }
    }
}
//
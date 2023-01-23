package com.example.wearit.ui


import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wearit.MainActivity
import com.example.wearit.WearItApp
import com.example.wearit.WearItScreen
import com.example.wearit.data.AppViewModel
import com.example.wearit.di.AppModule
import com.example.wearit.ui.theme.WearItTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class PickerScreenTest{

    @get:Rule(order=0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order=1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp(){
        hiltRule.inject()

        composeRule.setContent {
            val navController = rememberNavController()
            val viewModel: AppViewModel = hiltViewModel<AppViewModel>()
            val uiState by viewModel.uiState.collectAsState()
            WearItTheme {
                NavHost(
                    navController = navController,
                    startDestination = WearItScreen.Picker.name
                ) {

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
                            drawSelection = { viewModel.drawItems() },
                            saveOutfit = { viewModel.saveOutfit() },
                            goToSettings = { navController.navigate(WearItScreen.Settings.name) }
                        )
                    }


                }
            }
        }
    }

    @Test
    fun clickForDrawing_drawn(){
        composeRule.onNodeWithTag("emptySelection").assertIsDisplayed()
        composeRule.onNodeWithTag("drawButton").performClick()
        composeRule.onNodeWithTag("emptySelection").assertIsDisplayed()

    }

    @Test
    fun clickForSaving_Empty(){
//        composeRule.onNodeWithTag("snackBarInfo").assertIsNotDisplayed()
        composeRule.onNodeWithTag("emptySelection").assertIsDisplayed()
        composeRule.onNodeWithTag("saveOutfit").performClick()
        composeRule.onNodeWithTag("emptySelection").assertIsDisplayed()
//        composeRule.onNodeWithTag("snackBarInfo").assertIsDisplayed()


    }

}
package com.example.wearit.ui


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Log

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import com.example.wearit.MainActivity
import com.example.wearit.WearItScreen
import com.example.wearit.data.*
import com.example.wearit.di.AppModule
import com.example.wearit.data.AppViewModel
import com.example.wearit.model.Category
import com.example.wearit.model.Item
import com.example.wearit.ui.theme.WearItTheme
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.internal.notifyAll
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

    @SuppressLint("StateFlowValueCalledInComposition")
    @Before
    fun setUp(){
        hiltRule.inject()
        composeRule.setContent {
            val navController = rememberNavController()
            val viewModel =  hiltViewModel<AppViewModel>()

            Log.d("size of items",viewModel.getAllItems.value.size.toString())

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
                                )
                            },
                            currentSelection = viewModel.uiState.value.currentSelection.mapNotNull {
                                    itemId ->
                                viewModel.getItemById(
                                    itemId
                                )
                            }
                            ,
                            changeSelectedItem = { category, next ->
                                viewModel.changeSelectedItem(
                                    category,
                                    next
                                )
                            },
                            drawSelection = { viewModel.drawItems() },
                            saveOutfit = { viewModel.saveOutfit() }
                        ) { navController.navigate(WearItScreen.Settings.name) }
                    }

                }



            }


        }


    }


    @Test
    fun clickingDraw_emptySelection_nothing(){
        composeRule.onNodeWithTag("drawButton").performClick()
        composeRule.onNodeWithTag("error").assertDoesNotExist()
    }


    @Test
    fun clickingSave_emptySelection_SnackBar(){
        composeRule.onNodeWithTag("saveOutfit").performClick()
        composeRule.onNodeWithTag("snackBarInfo").onChild().assertTextEquals("Outfit can't be empty")
    }


}
package com.example.wearit.ui


import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.wearit.MainActivity
import com.example.wearit.WearItScreen
import com.example.wearit.data.*
import com.example.wearit.di.AppModule
import com.example.wearit.ui.theme.WearItTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.example.wearit.R
import com.example.wearit.model.AppViewModel
import org.awaitility.Awaitility
import java.util.concurrent.TimeUnit

@HiltAndroidTest
@UninstallModules(AppModule::class)
class PickerScreenTest{

    @get:Rule(order=0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order=1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private val internalStorageHelper = InternalStorageHelper(InstrumentationRegistry.getInstrumentation().targetContext)

    @SuppressLint("StateFlowValueCalledInComposition")
    @Before
    fun setUp(){
        val testItem1 = BitmapFactory.decodeResource(InstrumentationRegistry.getInstrumentation().targetContext.resources, R.drawable.test_item_1)
        "filename: "+internalStorageHelper.savePhoto(testItem1, "testItem1.png")
        val testItem2 = BitmapFactory.decodeResource(InstrumentationRegistry.getInstrumentation().targetContext.resources, R.drawable.test_item_2)
        "filename: "+internalStorageHelper.savePhoto(testItem2, "testItem2.png")
        val testItem3 = BitmapFactory.decodeResource(InstrumentationRegistry.getInstrumentation().targetContext.resources, R.drawable.test_item_3)
        "filename: "+internalStorageHelper.savePhoto(testItem3, "testItem3.png")

        Awaitility.setDefaultPollInterval(500, TimeUnit.MILLISECONDS)

        hiltRule.inject()
        composeRule.setContent {
            val navController = rememberNavController()
            val viewModel =  hiltViewModel<AppViewModel>()

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
        composeRule.onNodeWithText("You have to draw your items first").assertExists()
    }


    @Test
    fun clickingSave_emptySelection_SnackBar(){
        composeRule.onNodeWithTag("saveOutfit").performClick()
        composeRule.onNodeWithTag("snackBarInfo").onChild().assertTextEquals("Outfit can't be empty")
    }


}
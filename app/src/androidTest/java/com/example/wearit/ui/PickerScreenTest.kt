package com.example.wearit.ui


import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.wearit.model.AppViewModel
import com.example.wearit.ui.theme.WearItTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.example.wearit.R
import org.awaitility.Awaitility
import org.junit.After
import java.util.concurrent.TimeUnit


@HiltAndroidTest
@UninstallModules(AppModule::class)
class PickerScreenTest{

    @get:Rule(order=0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order=1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private val internalStorageHelper = InternalStorageHelper(InstrumentationRegistry.getInstrumentation().targetContext)

    private lateinit var viewModel: AppViewModel

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
            viewModel =  hiltViewModel<AppViewModel>()
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

    @After
    fun end() {
        val outfitsSavedDuringTests = viewModel.getAllOutfits.value
        outfitsSavedDuringTests.forEach {
            viewModel.deleteOutfit(it)
        }
    }


    @Test
    fun clickDraw_selectionDrawn() {
        composeRule.onNodeWithTag("drawButton").performClick()

        composeRule.onAllNodesWithTag("selectedItem").onFirst().assertExists()
    }

    @Test
    fun clickingSave_Selection_SnackBar() {
        composeRule.onNodeWithTag("drawButton").performClick()

        composeRule.onNodeWithTag("saveOutfit")
            .performClick()

        composeRule.onNodeWithTag("snackBarInfo")
            .onChild()
            .assertTextEquals("Outfit saved properly")
    }

}
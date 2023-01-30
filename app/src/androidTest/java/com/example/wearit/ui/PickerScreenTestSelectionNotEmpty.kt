package com.example.wearit.ui


import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.wearit.MainActivity
import com.example.wearit.R
import com.example.wearit.WearItScreen
import com.example.wearit.data.*
import com.example.wearit.di.AppModule
import com.example.wearit.model.AppViewModel
import com.example.wearit.model.Item
import com.example.wearit.model.Outfit
import com.example.wearit.ui.theme.WearItTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import getDrawnItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.awaitility.Awaitility
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

@HiltAndroidTest
@UninstallModules(AppModule::class)
class PickerScreenTestSelectionNotEmpty {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private val internalStorageHelper =
        InternalStorageHelper(InstrumentationRegistry.getInstrumentation().targetContext)

    @SuppressLint("StateFlowValueCalledInComposition")
    @Before
    fun setUp() {

        val testItem1 = BitmapFactory.decodeResource(
            InstrumentationRegistry.getInstrumentation().targetContext.resources,
            R.drawable.test_item_1
        )
        "filename: " + internalStorageHelper.savePhoto(testItem1, "testItem1.png")
        val testItem2 = BitmapFactory.decodeResource(
            InstrumentationRegistry.getInstrumentation().targetContext.resources,
            R.drawable.test_item_2
        )
        "filename: " + internalStorageHelper.savePhoto(testItem2, "testItem2.png")
        val testItem3 = BitmapFactory.decodeResource(
            InstrumentationRegistry.getInstrumentation().targetContext.resources,
            R.drawable.test_item_3
        )
        "filename: " + internalStorageHelper.savePhoto(testItem3, "testItem3.png")

        Awaitility.setDefaultPollInterval(500, TimeUnit.MILLISECONDS)


        hiltRule.inject()
        composeRule.setContent {
            val navController = rememberNavController()
            val viewModel = hiltViewModel<AppViewModel>()

//            viewModel.uiState.value.currentSelection = viewModel.getAllItems.value.map {
//                    itemId ->
//                itemId.id
//            }

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
                            currentSelection = viewModel.getAllItems.collectAsState().value.map { itemId ->
                                itemId
                            },
                            changeSelectedItem = { category, next ->
                                viewModel.changeSelectedItem(
                                    category,
                                    next
                                )
                            },
                            drawSelection = {
                                val itemList: List<Item> = viewModel.getAllItems.value
                                val newCurrentSelection = getDrawnItems(itemList)

                                viewModel.get_uiState().update { currentState ->
                                    currentState.copy(
                                        currentSelection = newCurrentSelection
                                    )
                                }
                                            },
                            saveOutfit = { ->
                                if (viewModel.get_uiState().value.currentSelection.isEmpty()) {
                                    null
                                }

                                var flag = false

                                if (!viewModel.getAllOutfits.value.any { it.itemsInOutfit.sorted() == viewModel.get_uiState().value.currentSelection.sorted() }) {

                                    val newOutfit = Outfit(
                                        id = 0,
                                        itemsInOutfit = viewModel.get_uiState().value.currentSelection
                                    )
                                    viewModel.viewModelScope.launch(Dispatchers.IO) {
                                        viewModel.getAppRepository().addOutfit(outfit = newOutfit)
                                    }

                                    flag = true
                                }
                                flag
                            },
                            goToSettings = { navController.navigate(WearItScreen.Settings.name) })

                    }


                }


            }


        }
    }


//    @After
//    fun end(){
//        internalStorageHelper.deleteFile("testItem1.png")
//        internalStorageHelper.deleteFile("testItem2.png")
//        internalStorageHelper.deleteFile("testItem3.png")
//    }


    @Test
    fun clickingDraw_Selection() {
        Thread.sleep(3000)
    }


    @Test
    fun clickingSave_Selection_SnackBar() {
        composeRule.onNodeWithTag("saveOutfit")
            .performClick()
        composeRule.onNodeWithTag("snackBarInfo")
            .onChild()
            .assertTextEquals("Outfit saved properly")
    }


    @Test
    fun double_save_of_selection_SnackBar() {
        composeRule.onNodeWithTag("saveOutfit")
            .performClick()
        Thread.sleep(4000)
        composeRule.onNodeWithTag("snackBarInfo")
            .onChild()
            .assertTextEquals("Outfit saved properly")

        composeRule.onNodeWithTag("saveOutfit")
            .performClick()
        composeRule.onNodeWithTag("snackBarInfo")
            .onChild()
            .assertTextEquals("Outfit already saved")
    }

    @Test
    fun clickingSave_NotEmptySelection_SnackBar() {
        composeRule.onNodeWithTag("drawButton")
            .performClick()
        Thread.sleep(4000)
        composeRule.onNodeWithTag("saveOutfit")
            .performClick()
        Thread.sleep(4000)
//        composeRule.onNodeWithTag("snackBarInfo")
//            .onChild().assertTextEquals("Outfit can't be empty")

    }


}

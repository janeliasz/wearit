package com.example.wearit.ui


import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import com.example.wearit.MainActivity
import com.example.wearit.WearItApp
import com.example.wearit.WearItScreen
import com.example.wearit.data.*
import com.example.wearit.di.AppModule
import com.example.wearit.di.TestAppModule
import com.example.wearit.model.Category
import com.example.wearit.model.Item
import com.example.wearit.ui.theme.WearItTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runners.Parameterized.BeforeParam


@HiltAndroidTest
@UninstallModules(AppModule::class)
class PickerScreenTest{
    private lateinit var database: AppDatabase
    private lateinit var itemDao: ItemDao
    private lateinit var outfitDao: OutfitDao
    private lateinit var repository: IAppRepository
    private lateinit var internalStorageHelper: IInternalStorageHelper
    private lateinit var storeSettings: IStoreSettings

    @get:Rule(order=0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order=1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @SuppressLint("StateFlowValueCalledInComposition")
    @Before()
    fun setUp(){
        hiltRule.inject()

        database = TestAppModule.provideAppDatabase(ApplicationProvider.getApplicationContext())
        itemDao = TestAppModule.provideItemDao(database)
        outfitDao = TestAppModule.provideOutfitDao(database)
        repository = TestAppModule.provideAppRepository(itemDao,outfitDao)
        insertItems()

        composeRule.setContent {
            val navController = rememberNavController()
            val viewModel: AppViewModel = hiltViewModel()

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
                            currentSelection = viewModel.uiState.value.currentSelection.mapNotNull{ itemId ->
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
                            saveOutfit = { viewModel.saveOutfit() }
                        ) { navController.navigate(WearItScreen.Settings.name) }
                    }


                }
            }
        }
    }

    fun insertItems(): Unit = runBlocking {
        val item1 =    Item(
            id = 1,
            name = "item-1",
            photoFilename = "item-1.png",
            category = Category.Headgear,
            isActive = true
        )

        val item2 = Item(
                id = 4,
                name = "item-4",
                photoFilename = "item-4.png",
                category = Category.Shorts,
                isActive = true
            )

        repository.addItem(item = item1)
        repository.addItem(item = item2)

    }


    @After
    fun tearDown(){
        database.close()
    }




    @Test
    fun clickingDraw_emptySelection(){
//
        composeRule.onNodeWithTag("error").assertDoesNotExist()
        composeRule.onNodeWithTag("drawButton").performClick()
        composeRule.onNodeWithTag("error").assertDoesNotExist()
    }

    @Test
    fun snackbarAfterClickingDraw_displayed_selectionNotEmpty(){
        composeRule.onNodeWithTag("snackBarInfo").assertDoesNotExist()
        composeRule.onNodeWithTag("saveOutfit").performClick()
        composeRule.onNodeWithTag("snackBarInfo").assertIsDisplayed()
        composeRule.onNodeWithTag("snackBarInfo").assertExists().onChild().assertTextEquals("Outfit saved properly")
    }

    @Test
    fun snackbarAfterClickingDraw_displayed_selectionAlreadySaved(){
        composeRule.onNodeWithTag("snackBarInfo").assertDoesNotExist()
        composeRule.onNodeWithTag("saveOutfit").performClick()
        composeRule.onNodeWithTag("snackBarInfo").assertIsDisplayed()
        composeRule.onNodeWithTag("snackBarInfo").assertExists().onChild().assertTextEquals("Outfit saved properly")
        Thread.sleep(2000)
    }

    @Test
    fun drawItems_drawed(){
        composeRule.onNodeWithTag("emptySelection").assertIsDisplayed()
        composeRule.onNodeWithTag("drawButton").performClick()

//        composeRule.onNodeWithTag("items").assertIsDisplayed()
        Thread.sleep(2000)
//        composeRule.onNodeWithTag("snackBarInfo").assertExists().onChild().assertTextEquals("Outfit saved properly")
    }




}
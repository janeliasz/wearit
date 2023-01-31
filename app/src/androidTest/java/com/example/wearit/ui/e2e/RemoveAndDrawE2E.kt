package com.example.wearit.ui.e2e


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
import com.example.wearit.model.Category
import com.example.wearit.model.Item
import com.example.wearit.ui.FavoritesScreen
import com.example.wearit.ui.PickerScreen
import com.example.wearit.ui.WardrobeScreen
import kotlinx.coroutines.runBlocking
import org.awaitility.Awaitility
import org.junit.After
import java.util.concurrent.TimeUnit


@HiltAndroidTest
@UninstallModules(AppModule::class)
class RemoveAndDrawE2E{

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
            val outfits by viewModel.getAllOutfits.collectAsState()

            println("przed runblocking")
            runBlocking {
                println("im here")
                viewModel.getAppRepository().addItem(Item(1,"testItem1", "testItem1.png", Category.Headgear, isActive = false))
                viewModel.getAppRepository().addItem(Item(2,"testItem2", "testItem2.png", Category.Headgear))
                viewModel.getAppRepository().addItem(Item(3,"testItem3", "testItem3.png", Category.Coat))
            }


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
                            itemsOfCurrentCategory = viewModel.getAllItems.value.filter { item -> item.category == viewModel.uiState.value.currentCategory },
                            saveItem = { bitmap -> viewModel.saveItem("test", bitmap) },
                            getItemPhotoByPhotoFilename = { itemId ->
                                viewModel.getItemPhotoByPhotoFilename(
                                    itemId
                                )
                            },
                            setActiveInactive = { viewModel.setItemActiveInactive(it) },
                            currentCategory = viewModel.uiState.value.currentCategory,
                            goToSingleItem = { itemId -> navController.navigate(WearItScreen.ItemInfo.name+"/${itemId}")},
                            goToFavorites = { navController.navigate(WearItScreen.Favorites.name)},
                            deleteItem = { viewModel.deleteItem(it) }
                        )
                    }

                    composable(WearItScreen.Favorites.name) {
                        FavoritesScreen(
                            goToPickerScreen = { navController.navigate(WearItScreen.Picker.name) },
                            goToWardrobe = { navController.navigate(WearItScreen.Wardrobe.name) },
                            outfits = outfits,
                            getItemById = { viewModel.getItemById(it) },
                            deleteOutfit={ viewModel.deleteOutfit(it) },
                            getItemPhotoByPhotoFilename = { itemId ->
                                viewModel.getItemPhotoByPhotoFilename(
                                    itemId
                                )!!
                            }
                        )
                    }

                }



            }


        }
    }

    @After
    fun end() {
        println("after")

        viewModel.getAllOutfits.value.forEach {
            viewModel.deleteOutfit(it)
        }

        viewModel.getAllItems.value.forEach { viewModel.deleteItem(it) }
    }

    @Test
    fun removeAndDraw() {
        composeRule.onNodeWithText("WARDROBE").performClick()

        composeRule.onNodeWithText("EDIT").performClick()

        composeRule.onNodeWithTag("testItem1-delete").performClick()

        composeRule.onNodeWithText("DELETE").performClick()

        composeRule.onNodeWithTag("testItem2-delete").performClick()

        composeRule.onNodeWithText("DELETE").performClick()

        composeRule.onNodeWithTag("Coat").performClick()

        composeRule.onNodeWithText("DRAW").performClick()

        composeRule.onNodeWithTag("drawButton").performClick()

        composeRule.onNodeWithTag("selectedItem-1").assertDoesNotExist()

        composeRule.onNodeWithTag("selectedItem-2").assertDoesNotExist()

        composeRule.onNodeWithTag("selectedItem-3").assertExists()

        composeRule.onNodeWithText("WARDROBE").performClick()

        composeRule.onNodeWithTag("testItem3").performClick()

        composeRule.onNodeWithText("DRAW").performClick()

        composeRule.onNodeWithTag("drawButton").performClick()

    }

}

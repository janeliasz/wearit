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
import kotlinx.coroutines.runBlocking
import org.awaitility.Awaitility
import org.junit.After
import java.util.concurrent.TimeUnit


@HiltAndroidTest
@UninstallModules(AppModule::class)
class WardrobeScreenTest{

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

            runBlocking {
                viewModel.getAppRepository().addItem(Item(1,"testItem1", "testItem1.png", Category.Headgear))
                viewModel.getAppRepository().addItem(Item(2,"testItem2", "testItem2.png", Category.Headgear))
                viewModel.getAppRepository().addItem(Item(3,"testItem3", "testItem3.png", Category.Coat))
            }

            WearItTheme {
                NavHost(
                    navController = navController,
                    startDestination = WearItScreen.Wardrobe.name
                ) {


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

                }



            }


        }


    }

    @After
    fun end() {
        viewModel.getAllOutfits.value.forEach {
            viewModel.deleteOutfit(it)
        }

        viewModel.getAllItems.value.forEach { viewModel.deleteItem(it) }
    }


    @Test
    fun itemRenders() {
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted {
            composeRule.onNodeWithTag("testItem1").assertExists()
        }
    }

    @Test
    fun clickEdit_deleteButtonVisible() {
        composeRule.onNodeWithText("EDIT").performClick()

        composeRule.onNodeWithTag("testItem1-delete").assertExists()
    }




}
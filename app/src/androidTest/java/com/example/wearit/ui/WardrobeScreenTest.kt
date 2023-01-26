package com.example.wearit.ui


import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Log
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
import com.example.wearit.data.AppViewModel
import com.example.wearit.ui.theme.WearItTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.example.wearit.R
import com.example.wearit.model.Category
import org.awaitility.Awaitility
import org.junit.BeforeClass
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltAndroidTest
@UninstallModules(AppModule::class)
class WardrobeScreenTest{

    @get:Rule(order=0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order=1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    val internalStorageHelper = InternalStorageHelper(InstrumentationRegistry.getInstrumentation().targetContext)

    @SuppressLint("StateFlowValueCalledInComposition")
    @Before
    fun setUp(){

        val testItemBitmap = BitmapFactory.decodeResource(InstrumentationRegistry.getInstrumentation().targetContext.resources, R.drawable.cargos);
        println("filename: "+internalStorageHelper.savePhoto(testItemBitmap, "cargos.png", InstrumentationRegistry.getInstrumentation().targetContext))

        hiltRule.inject()
        composeRule.setContent {
            val navController = rememberNavController()
            val viewModel =  hiltViewModel<AppViewModel>()
            viewModel.goToCategory(Category.Trousers)
            val items by viewModel.getAllItems.collectAsState()
            Log.d("size of items",viewModel.getAllItems.value.size.toString())
            Log.d("item: ",viewModel.getAllItems.value[0].id.toString())
            Log.d("item: ",viewModel.getAllItems.value[0].name)
            Log.d("item: ",viewModel.getAllItems.value[0].photoFilename)
            Log.d("item: ",viewModel.getAllItems.value[0].category.toString())
            Log.d("item: ",viewModel.getAllItems.value[0].isActive.toString())
            Log.d("items of  current category: ", items.filter { item -> item.category == viewModel.uiState.value.currentCategory }.size.toString())

            WearItTheme {
                NavHost(
                    navController = navController,
                    startDestination = WearItScreen.Wardrobe.name
                ) {


                    composable(WearItScreen.Wardrobe.name) {
                        WardrobeScreen(
                            onCategoryChange = { viewModel.goToCategory(it) },
                            goToPickerScreen = { navController.navigate(WearItScreen.Picker.name) },
                            itemsOfCurrentCategory = items.filter { item -> item.category == viewModel.uiState.value.currentCategory },
                            saveItem = { bitmap -> viewModel.saveItem("test", bitmap) },
                            getItemPhotoByPhotoFilename = { itemId ->
                                viewModel.getItemPhotoByPhotoFilename(
                                    itemId
                                )!!
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


    @Test
    fun dupa(){
        Awaitility.setDefaultPollInterval(500, TimeUnit.MILLISECONDS)

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted {
            composeRule.onNodeWithTag("item-1").assertExists()
        }
    }

    @Test
    fun dupa2(){
        Awaitility.setDefaultPollInterval(500, TimeUnit.MILLISECONDS)

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted {
            composeRule.onNodeWithTag("item-1").assertExists()
        }
    }


}
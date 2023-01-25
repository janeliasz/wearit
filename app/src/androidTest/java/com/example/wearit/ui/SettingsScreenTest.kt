package com.example.wearit.ui

import SettingsScreen
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wearit.MainActivity
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
class SettingsScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @SuppressLint("StateFlowValueCalledInComposition")
    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.setContent {
            val navController = rememberNavController()
            val viewModel = hiltViewModel<AppViewModel>()
            val isAppInDarkTheme by viewModel.getIsAppInDarkTheme.collectAsState()

            WearItTheme {
                NavHost(
                    navController = navController,
                    startDestination = WearItScreen.Settings.name
                ) {

                    composable(WearItScreen.Settings.name) {
                        SettingsScreen(
                            isAppInDarkTheme = isAppInDarkTheme,
                            switchTheme = { darkMode -> viewModel.switchTheme(darkMode) },
                            goToPicker = { navController.navigate(WearItScreen.Picker.name) }
                        )
                    }


                }


            }


        }


    }


    @Test
    fun clickingDarkMode_DarkModeON() {
        composeRule.onNodeWithTag("darkModeSwitch").assertIsToggleable()
        composeRule.onNodeWithTag("darkModeSwitch").performGesture {  swipeRight() }
        Thread.sleep(3000)
    }



}
package com.example.wearit.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun PickerScreen(
    onButtonClick: () -> Unit
) {
    Column {
        Text(text = "This is Picker screen.")
        Button(onClick = onButtonClick) {
            Text(text = "Go to Wardrobe")
        }
    }
}
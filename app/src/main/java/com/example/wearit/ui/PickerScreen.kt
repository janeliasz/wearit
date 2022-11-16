package com.example.wearit.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.wearit.model.Item

@Composable
fun PickerScreen(
    onButtonClick: () -> Unit,
    currentSelection: List<Item>
) {
    Column {
        Text(text = "This is Picker screen.")
        Button(onClick = onButtonClick) {
            Text(text = "Go to Wardrobe")
        }

        LazyColumn {
            items(currentSelection) { item ->
                Row {
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = "PREV")
                    }

                    Image(painter = painterResource(id = item.photoId), contentDescription = item.name)

                    Button(onClick = { /*TODO*/ }) {
                        Text(text = "PREV")
                    }
                }
            }
        }
    }
}
package com.example.wearit.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wearit.R
import com.example.wearit.components.ButtonType
import com.example.wearit.components.MasterButton

@Composable
fun FavoritesScreen(
    goToPickerScreen: () -> Unit,
    goToWardrobe:()->Unit,
){
    Scaffold(
        content = { innerPadding ->
            //giving padding to whole content so it doesnt overlap with bottomBar
            FavoritesContent()
        },
        bottomBar = {
            BottomBar(
                goToPickerScreen = goToPickerScreen,
                goToWardrobe = goToWardrobe
            )
        }
    )

}

@Composable
fun FavoritesContent(){



}

@Composable
fun BottomBar(
    goToPickerScreen: () -> Unit,
    goToWardrobe:() -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        MasterButton(
            type = ButtonType.RED,
            onClick = goToPickerScreen,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(10.dp),
            icon = R.drawable.dice,
            text = "DRAW",
        )

        MasterButton(
            type = ButtonType.WHITE,
            onClick = goToWardrobe,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(10.dp),
            icon = null,
            text = "Wardrobe",
        )

    }
}

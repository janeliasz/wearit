package com.example.wearit.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wearit.R
import com.example.wearit.components.ButtonType
import com.example.wearit.components.MasterButton
import com.example.wearit.model.Category
import com.example.wearit.model.Item

@Composable
fun PickerScreen(
    goToWardrobe: () -> Unit,
    getItemPhotoByPhotoFilename: (itemId: String) -> Bitmap,
    currentSelection: List<Item>,
    changeSelectedItem: (category: Category, next: Boolean) -> Unit,
    drawSelection: () -> Unit,
    saveOutfit: () -> Unit,
    goToSettings: () -> Unit
) {
    Scaffold(
        content = { innerPadding ->
            //giving padding to whole content so it doesnt overlap with bottomBar
            PickerContent(
                getItemPhotoByPhotoFilename = getItemPhotoByPhotoFilename,
                currentSelection = currentSelection,
                changeSelectedItem = changeSelectedItem,
                goToSettings = goToSettings
            )

        },
        bottomBar = {
            BottomBarPicker(
                drawSelection = drawSelection,
                saveOutfit = saveOutfit,
                goToWardrobe = goToWardrobe
            )
        }
    )

}

@Composable
fun PickerContent(
    getItemPhotoByPhotoFilename: (itemId: String) -> Bitmap,
    currentSelection: List<Item>,
    changeSelectedItem: (category: Category, next: Boolean) -> Unit,
    goToSettings: () -> Unit

) {
    Box(
        modifier = Modifier
            .fillMaxWidth().height(75.dp)
            .padding(10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.wearit),
            contentDescription = "Logo text",
            modifier = Modifier.align(Alignment.Center),
            contentScale = ContentScale.Fit

        )

        Image(
            painter = painterResource(id = R.drawable.settings),
            contentDescription = "Settings",
            modifier = Modifier
                .clickable { goToSettings() }
                .align(Alignment.CenterEnd),
            contentScale = ContentScale.Fit
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {


        LazyColumn {
            items(currentSelection) { item ->
                Row {
                    Button(onClick = { changeSelectedItem(item.category, false) }) {
                        Text(text = "PREV")
                    }

                    Image(
                        bitmap = getItemPhotoByPhotoFilename(item.photoFilename).asImageBitmap(),
                        contentDescription = item.name,
                        modifier = Modifier.size(100.dp)
                    )

                    Button(onClick = { changeSelectedItem(item.category, true) }) {
                        Text(text = "NEXT")
                    }
                }
            }
        }


    }
}

@Composable
fun BottomBarPicker(
    drawSelection: () -> Unit,
    saveOutfit: () -> Unit,
    goToWardrobe: () -> Unit
) {
    Divider(color = MaterialTheme.colors.primary, thickness = 5.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(80.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        MasterButton(
            onClick = drawSelection,
            icon = R.drawable.dice,
            modifier = Modifier.weight(1f, fill = true),
            text = "DRAW",
            fontSize = 15.sp
        )


        MasterButton(
            type = ButtonType.WHITE,
            onClick = saveOutfit,
            icon = R.drawable.diskette,
        )

        MasterButton(
            onClick = goToWardrobe,
            modifier = Modifier.weight(1f, fill = true),
            icon = null,
            text = "WARDROBE",
            fontSize = 15.sp
        )
    }

}
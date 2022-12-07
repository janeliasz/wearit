package com.example.wearit.ui

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.example.wearit.R
import com.example.wearit.components.ButtonType
import com.example.wearit.components.MasterButton
import com.example.wearit.model.Item
import com.example.wearit.model.Outfit

@Composable
fun FavoritesScreen(
    goToPickerScreen: () -> Unit,
    goToWardrobe:()->Unit,
    outfits: List<Outfit>,
    getItemById: (Int) -> Item?,
    getItemPhotoByPhotoFilename: (itemId: String) -> Bitmap,
){
    Log.d("outfits",outfits.toString())
    Scaffold(
        content = { innerPadding ->
            //giving padding to whole content so it doesnt overlap with bottomBar
            FavoritesContent(
                outfits = outfits,
                getItemById = getItemById,
                getItemPhotoByPhotoFilename = getItemPhotoByPhotoFilename
            )
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
fun FavoritesContent(
    outfits: List<Outfit>,
    getItemById:(Int)-> Item?,
    getItemPhotoByPhotoFilename: (itemId: String) -> Bitmap
){
    Text(text = "Im here")
    LazyColumn(

    ){
        items(outfits){ outfit ->
            LazyRow(

            ){

                items(outfit.itemsInOutfit){item->
                    val finalItem = getItemById(item)

                        Image(
                            bitmap = getItemPhotoByPhotoFilename(finalItem!!.photoFilename).asImageBitmap(),
                            contentDescription = finalItem.name,
                            modifier = Modifier.size(100.dp)
                        )

                }

            }
        }

    }


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

package com.example.wearit.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

@SuppressLint("SuspiciousIndentation")
@Composable
fun FavoritesContent(
    outfits: List<Outfit>,
    getItemById:(Int)-> Item?,
    getItemPhotoByPhotoFilename: (itemId: String) -> Bitmap
){
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 50.dp, vertical = 100.dp),
        verticalArrangement = Arrangement.spacedBy(50.dp),
    ){
        items(outfits){ outfit ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(5.dp,5.dp)
                    .clip(shape = RoundedCornerShape(50.dp))
                    .fillMaxWidth()
                    .border(
                        width = 5.dp,
                        color = MaterialTheme.colors.primary.copy(alpha = LocalContentAlpha.current),
                        shape = RoundedCornerShape(50.dp)
                    )

            ){
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp,top=5.dp, bottom = 5.dp, end = 24.dp),



                ){

                    items(outfit.itemsInOutfit){item->
                        val finalItem = getItemById(item)
                        Image(
                            bitmap = getItemPhotoByPhotoFilename(finalItem!!.photoFilename).asImageBitmap(),
                            contentDescription = finalItem.name,
                            modifier = Modifier.size(width = 200.dp, height = 340.dp)
                        )

                    }

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
    Column(modifier = Modifier.background(color= Color.White)){
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

}
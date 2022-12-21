package com.example.wearit.ui

import android.graphics.Bitmap
import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
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
            .fillMaxWidth(),
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


    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top= 50.dp, bottom = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(currentSelection) { item ->

            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                GifImage(
                    gif = R.drawable.left,
                    modifier = Modifier
                        .clickable { changeSelectedItem(item.category, false) }
                        .align(Alignment.CenterVertically)
                        .padding(10.dp).weight(0.2f,fill=true)

                )


                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(10.dp)
                        .clip(shape = RoundedCornerShape(50.dp))
                        .align(Alignment.CenterVertically)
                        .weight(0.6f, fill = true).height(300.dp)
                        .border(
                            width = 5.dp,
                            color = MaterialTheme.colors.primary.copy(alpha = LocalContentAlpha.current),
                            shape = RoundedCornerShape(50.dp)
                        )
                ) {
                    Image(
                        bitmap = getItemPhotoByPhotoFilename(item.photoFilename).asImageBitmap(),
                        contentDescription = item.name,
                        modifier = Modifier.padding(20.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                GifImage(
                    gif = R.drawable.right,
                    modifier = Modifier
                        .clickable { changeSelectedItem(item.category, true) }
                        .align(Alignment.CenterVertically)
                        .padding(10.dp).weight(0.2f,fill=true)

                )


            }
        }
    }

}

@Composable
fun GifImage(
    modifier: Modifier = Modifier,
    gif: Int,
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 26) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context).data(data = gif).apply(block = {
                size(Size.ORIGINAL)
            }).build(), imageLoader = imageLoader
        ),
        contentDescription = null,
        modifier = modifier.fillMaxWidth(),
        contentScale = ContentScale.Fit
    )
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
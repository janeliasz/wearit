package com.example.wearit.ui

import ImageIcon
import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.example.wearit.R
import com.example.wearit.components.ButtonType
import com.example.wearit.components.MasterButton
import com.example.wearit.model.Item
import com.example.wearit.model.Outfit
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@Composable
fun FavoritesScreen(
    goToPickerScreen: () -> Unit,
    goToWardrobe: () -> Unit,
    outfits: List<Outfit>,
    getItemById: (Int) -> Item?,
    getItemPhotoByPhotoFilename: (itemId: String) -> Bitmap,
    deleteOutfit: (Outfit) -> Unit
) {
    Scaffold(
        content = { innerPadding ->
            //giving padding to whole content so it doesnt overlap with bottomBar
            FavoritesContent(
                outfits = outfits,
                getItemById = getItemById,
                getItemPhotoByPhotoFilename = getItemPhotoByPhotoFilename,
                deleteOutfit = deleteOutfit,
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
    getItemById: (Int) -> Item?,
    getItemPhotoByPhotoFilename: (itemId: String) -> Bitmap,
    deleteOutfit: (Outfit) -> Unit,
) {
    var editing: Boolean by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 75.dp)
    ) {
        Box(modifier = Modifier.padding(top = 30.dp, bottom = 20.dp, start = 35.dp, end = 35.dp)) {
            MasterButton(
                type = ButtonType.WHITE,
                onClick = { editing = !editing },
                modifier = Modifier
                    .fillMaxWidth(),
                icon = R.drawable.editing,
                text = if (!editing) "EDIT" else "DONE",
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 35.dp, vertical = 50.dp),
            verticalArrangement = Arrangement.spacedBy(50.dp),
        ) {
            items(outfits) { outfit ->
                SingleOutfit(
                    outfit = outfit,
                    getItemById = getItemById,
                    getItemPhotoByPhotoFilename = getItemPhotoByPhotoFilename,
                    editing = editing,
                    deleteOutfit = deleteOutfit
                )
            }
        }

    }


}

@Composable
fun SingleOutfit(
    outfit: Outfit,
    getItemById: (Int) -> Item?,
    getItemPhotoByPhotoFilename: (itemId: String) -> Bitmap,
    editing: Boolean,
    deleteOutfit: (Outfit) -> Unit
) {
    val outfitOpacity: Float by animateFloatAsState(
        targetValue = if (!editing) 1f else 0.6f,
        animationSpec = tween(
            durationMillis = 500,
            easing = LinearEasing,
        )
    )

    var isDeleteOutfitDialogOpen by remember { mutableStateOf(false) }

    if (isDeleteOutfitDialogOpen) {
        DeleteOutfitDialog(
            closeDialog = { isDeleteOutfitDialogOpen = false },
            deleteOutfit = deleteOutfit,
            outfit = outfit,
        )
    }

    Box {

        if(editing){
            ImageIcon(
                modifier = Modifier
                    .align(Alignment.TopEnd).padding(top=5.dp),
                tickOpacity = 1f,
                size = 35.dp,
                icon = R.drawable.close,
                onClick = {
                    if (editing) isDeleteOutfitDialogOpen = !isDeleteOutfitDialogOpen
                }
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(0.dp, 5.dp)
                .clip(shape = RoundedCornerShape(50.dp))
                .fillMaxWidth()
                .border(
                    width = 5.dp,
                    color = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(50.dp)
                )
                .alpha(outfitOpacity)

        ) {

            MakeHorizontalPager(
                outfit.itemsInOutfit,
                getItemById,
                getItemPhotoByPhotoFilename,
                outfitOpacity
            )

        }
    }


}

@Composable
fun DeleteOutfitDialog(
    closeDialog: () -> Unit,
    deleteOutfit: (Outfit) -> Unit,
    outfit: Outfit,
) {
    Dialog(
        onDismissRequest = closeDialog,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),

                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(
                    text = "CONFIRM DELETE:",
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(0.dp, 15.dp, 0.dp, 5.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 5.dp, 0.dp, 15.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    MasterButton(
                        type = ButtonType.WHITE,
                        onClick = closeDialog,
                        text = "CANCEL",
                        icon = null,
                        modifier = Modifier
                    )

                    MasterButton(
                        type = ButtonType.RED,
                        onClick = {
                            deleteOutfit(outfit)
                            closeDialog()
                        },
                        text = "DELETE",
                        icon = null,
                        modifier = Modifier
                    )
                }
            }

        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun MakeHorizontalPager(
    itemsInOutfit: List<Int>,
    getItemById: (Int) -> Item?,
    getItemPhotoByPhotoFilename: (itemId: String) -> Bitmap,
    outfitOpacity: Float

) {
    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .alpha(outfitOpacity)
    ) {
        HorizontalPager(
            count = itemsInOutfit.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
        ) { currentPage ->

            val finalItem = getItemById(itemsInOutfit[currentPage])

            if (finalItem != null) {
                Image(
                    bitmap = getItemPhotoByPhotoFilename(finalItem.photoFilename).asImageBitmap(),
                    contentDescription = finalItem.name,
                    modifier = Modifier
                        .size(width = 200.dp, height = 340.dp)
                        .padding(bottom = 5.dp, top = 5.dp)
                        .alpha(outfitOpacity)
                )
            }

        }
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, bottom = 15.dp, end = 10.dp)
        ) {
            PagerIndicator(pagerState = pagerState, indicatorCount = itemsInOutfit.size) {
                coroutineScope.launch {
                    pagerState.scrollToPage(it)
                }
            }
        }
    }


}

@Composable
fun BottomBar(
    goToPickerScreen: () -> Unit,
    goToWardrobe: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.background)
            .padding(10.dp)
            .height(100.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        MasterButton(
            onClick = goToPickerScreen,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            icon = R.drawable.dice,
            text = "DRAW",
        )

        MasterButton(
            type = ButtonType.WHITE,
            onClick = goToWardrobe,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            icon = null,
            text = "Wardrobe",
        )

    }


}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun PagerIndicator(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    indicatorCount: Int = 5,
    indicatorSize: Dp = 16.dp,
    indicatorShape: Shape = CircleShape,
    space: Dp = 8.dp,
    activeColor: Color = Color(0xffEC407A),
    inActiveColor: Color = Color.LightGray,
    onClick: ((Int) -> Unit)? = null
) {

    val listState = rememberLazyListState()

    val totalWidth: Dp = indicatorSize * indicatorCount + space * (indicatorCount - 1)

    val currentItem by remember {
        derivedStateOf {
            pagerState.currentPage
        }
    }

    val itemCount = pagerState.pageCount


    LazyRow(
        modifier = modifier.width(totalWidth),
        state = listState,
        contentPadding = PaddingValues(vertical = space),
        horizontalArrangement = Arrangement.spacedBy(space),

        ) {
        indicatorItems(
            itemCount,
            currentItem,
            indicatorCount,
            indicatorShape,
            activeColor,
            inActiveColor,
            indicatorSize,
            onClick
        )
    }


}

private fun LazyListScope.indicatorItems(
    itemCount: Int,
    currentItem: Int,
    indicatorCount: Int,
    indicatorShape: Shape,
    activeColor: Color,
    inActiveColor: Color,
    indicatorSize: Dp,
    onClick: ((Int) -> Unit)?
) {
    items(itemCount) { index ->

        val isSelected = (index == currentItem)

        // Index of item in center when odd number of indicators are set
        // for 5 indicators this is 2nd indicator place
        val centerItemIndex = indicatorCount / 2

        val right1 =
            (currentItem < centerItemIndex &&
                    index >= indicatorCount - 1)

        val right2 =
            (currentItem >= centerItemIndex &&
                    index >= currentItem + centerItemIndex &&
                    index < itemCount - centerItemIndex + 1)
        val isRightEdgeItem = right1 || right2

        // Check if this item's distance to center item is smaller than half size of
        // the indicator count when current indicator at the center or
        // when we reach the end of list. End of the list only one item is on edge
        // with 10 items and 7 indicators
        // 7-3= 4th item can be the first valid left edge item and
        val isLeftEdgeItem =
            index <= currentItem - centerItemIndex &&
                    currentItem > centerItemIndex &&
                    index < itemCount - indicatorCount + 1

        Box(
            modifier = Modifier
                .graphicsLayer {
                    val scale = if (isSelected) {
                        1f
                    } else if (isLeftEdgeItem || isRightEdgeItem) {
                        .5f
                    } else {
                        .8f
                    }
                    scaleX = scale
                    scaleY = scale

                }

                .clip(indicatorShape)
                .size(indicatorSize)
                .background(
                    if (isSelected) activeColor else inActiveColor,
                    indicatorShape
                )
                .then(
                    if (onClick != null) {
                        Modifier
                            .clickable {
                                onClick.invoke(index)
                            }
                    } else Modifier
                )
        )
    }
}


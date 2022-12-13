package com.example.wearit.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
) {
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
    getItemById: (Int) -> Item?,
    getItemPhotoByPhotoFilename: (itemId: String) -> Bitmap
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 50.dp, vertical = 100.dp),
        verticalArrangement = Arrangement.spacedBy(50.dp),
    ) {
        items(outfits) { outfit ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(5.dp, 5.dp)
                    .clip(shape = RoundedCornerShape(50.dp))
                    .fillMaxWidth()
                    .border(
                        width = 5.dp,
                        color = MaterialTheme.colors.primary.copy(alpha = LocalContentAlpha.current),
                        shape = RoundedCornerShape(50.dp)
                    )

            ) {

                MakeHorizontalPager(outfit.itemsInOutfit, getItemById, getItemPhotoByPhotoFilename);

            }
        }
    }

}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun MakeHorizontalPager(
    itemsInOutfit: List<Int>,
    getItemById: (Int) -> Item?,
    getItemPhotoByPhotoFilename: (itemId: String) -> Bitmap

) {
    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalPager(
            count = itemsInOutfit.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 10.dp, bottom = 5.dp, end = 24.dp)
        ) { currentPage ->
            Row() {
                val finalItem = getItemById(itemsInOutfit[currentPage])
                Image(
                    bitmap = getItemPhotoByPhotoFilename(finalItem!!.photoFilename).asImageBitmap(),
                    contentDescription = finalItem.name,
                    modifier = Modifier
                        .size(width = 150.dp, height = 250.dp)
                )

            }

        }
        Box(contentAlignment=Alignment.BottomCenter, modifier = Modifier.fillMaxWidth().padding(start = 10.dp, bottom = 15.dp, end = 10.dp)){
            PagerIndicator(pagerState = pagerState, indicatorCount = itemsInOutfit.size ) {
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
    Column(modifier = Modifier.background(color = Color.White)) {
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
    orientation: IndicatorOrientation = IndicatorOrientation.Horizontal,
    onClick: ((Int) -> Unit)? = null
) {

    val listState = rememberLazyListState()

    val totalWidth: Dp = indicatorSize * indicatorCount + space * (indicatorCount - 1)
    val widthInPx = LocalDensity.current.run { indicatorSize.toPx() }

    val currentItem by remember {
        derivedStateOf {
            pagerState.currentPage
        }
    }

    val itemCount = pagerState.pageCount


    if (orientation == IndicatorOrientation.Horizontal) {
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
    } else {
        LazyColumn(
            modifier = modifier.height(totalWidth),
            state = listState,
            contentPadding = PaddingValues(horizontal = space),
            verticalArrangement = Arrangement.spacedBy(space),
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

enum class IndicatorOrientation {
    Horizontal
}
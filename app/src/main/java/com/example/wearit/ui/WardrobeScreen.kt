package com.example.wearit.ui

import androidx.compose.foundation.*
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.wearit.R
import com.example.wearit.model.Category
import com.example.wearit.model.Item
import java.util.*

@Composable
fun WardrobeScreen(
    goToPickerScreen: () -> Unit,
    onCategoryChange: (category: Category) -> Unit,
    itemsOfCurrentCategory: List<Item>?,
    saveItem: (bitmap: Bitmap) -> Unit,
    getItemPhotoByPhotoFilename: (itemId: String) -> Bitmap,
    setActiveInactive: (item: Item) -> Unit,
    currentCategory: Category,
) {

    val listOfCategories = Category.values().asList()
    Scaffold(
        content = { innerPadding ->
            //giving padding to whole content so it doesnt overlap with bottomBar
            WardrobePageContent(
                innerPadding = innerPadding,
                onCategoryChange = onCategoryChange,
                listOfCategories = listOfCategories,
                itemsOfCurrentCategory = itemsOfCurrentCategory,
                saveItem = saveItem,
                getItemPhotoByPhotoFilename = getItemPhotoByPhotoFilename,
                setActiveInactive = setActiveInactive,
                currentCategory = currentCategory
            )
        },
        bottomBar = {
            BottomBarSpace(
                goToPickerScreen = goToPickerScreen

            )
        }
    )
}

@Composable
fun WardrobePageContent(
    innerPadding: PaddingValues,
    onCategoryChange: (category: Category) -> Unit,
    listOfCategories: List<Category>,
    itemsOfCurrentCategory: List<Item>?,
    saveItem: (bitmap: Bitmap) -> Unit,
    getItemPhotoByPhotoFilename: (itemId: String) -> Bitmap,
    setActiveInactive: (item: Item) -> Unit,
    currentCategory: Category

) {
    Box(modifier = Modifier.padding(innerPadding)) {
        Column() {
            WardrobeNavigationSection(
                saveItem = saveItem,
            )
            WardrobeClothesListSection(
                onCategoryChange = onCategoryChange,
                listOfCategories = listOfCategories,
                itemsOfCurrentCategory = itemsOfCurrentCategory,
                getItemPhotoByPhotoFilename = getItemPhotoByPhotoFilename,
                setActiveInactive = setActiveInactive,
                currentCategory = currentCategory,
            )
        }
    }
}

@Composable
fun WardrobeClothesListSection(
    onCategoryChange: (category: Category) -> Unit,
    listOfCategories: List<Category>,
    itemsOfCurrentCategory: List<Item>?,
    getItemPhotoByPhotoFilename: (itemId: String) -> Bitmap,
    setActiveInactive: (item: Item) -> Unit,
    currentCategory: Category

) {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        WardrobeListOfCategories(
            onCategoryChange = onCategoryChange,
            listOfCategories = listOfCategories,
            currentCategory = currentCategory
        )
        WardrobeListOfItemsFromCurrentCategory(
            itemsOfCurrentCategory = itemsOfCurrentCategory,
            getItemPhotoByPhotoFilename = getItemPhotoByPhotoFilename,
            setActiveInactive = setActiveInactive
        )
    }
}

@Composable
fun WardrobeListOfCategories(
    onCategoryChange: (category: Category) -> Unit,
    listOfCategories: List<Category>,
    currentCategory: Category,
) {
    val columnPadding = 7.dp
    Column {



        LazyColumn {

            itemsIndexed(listOfCategories) { index, category ->

                TextButton(
                    modifier = Modifier
                        .animateContentSize()
                        .size(if (category == currentCategory) 80.dp else 60.dp)
                        .clip(RoundedCornerShape(0.dp, 50.dp, 50.dp, 0.dp))
                        .background(if (category == currentCategory) MaterialTheme.colors.surface else MaterialTheme.colors.onSurface)
                        .border(
                            width = 9.dp,
                            color = MaterialTheme.colors.primary,
                            shape = RoundedCornerShape(0.dp, 50.dp, 50.dp, 0.dp)
                        ),

                    onClick = { onCategoryChange(category) }
                ) {
                    Icon(
                        painter = painterResource(id = category.icon),
                        tint = MaterialTheme.colors.primary,
                        contentDescription = "$category",
                        modifier = Modifier
                            .offset(x = (-4).dp)
                            .padding(10.dp)
                    )
                }
            }

        }


    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WardrobeListOfItemsFromCurrentCategory(
    itemsOfCurrentCategory: List<Item>?,
    getItemPhotoByPhotoFilename: (itemId: String) -> Bitmap,
    setActiveInactive: (item: Item) -> Unit,

    ) {
    Box(
        Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopCenter

    ) {
        if (itemsOfCurrentCategory != null) {
            LazyVerticalGrid(
                cells = GridCells.Fixed(2)
            )
            {
                items(itemsOfCurrentCategory) { item ->
                    SingleClothItem(
                        item = item,
                        getItemPhotoByPhotoFilename = getItemPhotoByPhotoFilename,
                        setActiveInactive = setActiveInactive
                    )
                }
            }
        } else {
            Text(text = "No items in category")
        }
    }
}

@Composable
fun SingleClothItem(
    item: Item,
    getItemPhotoByPhotoFilename: (itemId: String) -> Bitmap,
    setActiveInactive: (item: Item) -> Unit,

    ) {

    Column(
        Modifier
            .padding(3.dp)
            .clickable { setActiveInactive(item) },
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Text(text = "${item.isActive}", textAlign = TextAlign.Center, modifier = Modifier
            .padding(5.dp, 0.dp, 5.dp, 10.dp))
        Image(
            bitmap = getItemPhotoByPhotoFilename(item.photoFilename).asImageBitmap(),
            contentDescription = item.name,
            modifier = Modifier
                .size(100.dp)
                .padding(5.dp, 10.dp, 5.dp, 0.dp)
        )
        Text(
            text = item.name, textAlign = TextAlign.Center, modifier = Modifier
                .padding(5.dp, 0.dp, 5.dp, 10.dp)

        )
    }
}

@Composable
fun WardrobeNavigationSection(
    saveItem: (bitmap: Bitmap) -> Unit,
) {
    val contentResolver = LocalContext.current.contentResolver
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            val bitmap = getBitmap(contentResolver, uri)
            if (bitmap != null) {
                saveItem(bitmap)
            }
        }
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = { imagePicker.launch("image/*") }) {
            Text(text = "Add")
        }
        Button(onClick = { /*TODO*/ }) {
            Text(text = "EDIT")
        }
    }
}


@Composable
fun BottomBarSpace(
    goToPickerScreen: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = { goToPickerScreen() }) {
            Text(text = "DRAW")
        }
        Button(onClick = { /*TODO*/ }) {
            Text(text = "FAVOURITES")
        }
    }
}

fun getBitmap(contentResolver: ContentResolver, fileUri: Uri?): Bitmap? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, fileUri!!))
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

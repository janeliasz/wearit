package com.example.wearit.ui

import androidx.compose.foundation.*
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.wearit.R
import com.example.wearit.components.ButtonType
import com.example.wearit.components.MasterButton
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
    goToSettings: () -> Unit,

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
                goToSettings = goToSettings
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
    goToSettings: () -> Unit,

    ) {
    Box(modifier = Modifier.padding(innerPadding)) {
        Column() {
            WardrobeNavigationSection(
                saveItem = saveItem,
                goToSettings = goToSettings
            )

            Divider(
                color = MaterialTheme.colors.primary,
                thickness = 4.dp,
                modifier = Modifier
                    .padding(20.dp, 0.dp, 20.dp, 10.dp)
                    .offset(y = -10.dp)
            )
            WardrobeClothesListSection(
                onCategoryChange = onCategoryChange,
                listOfCategories = listOfCategories,
                itemsOfCurrentCategory = itemsOfCurrentCategory,
                getItemPhotoByPhotoFilename = getItemPhotoByPhotoFilename,
                setActiveInactive = setActiveInactive
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
    ) {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        WardrobeListOfCategories(
            onCategoryChange = onCategoryChange,
            listOfCategories = listOfCategories,
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
) {
    Column() {
        Column {
            LazyColumn {
                items(listOfCategories) { category ->
                    Button(onClick = { onCategoryChange(category) }) {
                        Text(text = "Go to $category")
                    }
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
    goToSettings: () -> Unit,


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
    Box(){
        Icon(painterResource(
            id = R.drawable.settings),
            contentDescription = "settings",
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.TopEnd)
                .padding(0.dp, 5.dp, 5 .dp, 0.dp)
                .clickable { goToSettings() }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly

        ) {
            MasterButton(
                type = ButtonType.RED,
                onClick = { imagePicker.launch("image/*") },
                modifier = Modifier,
                icon = R.drawable.camera,
                text = "ADD",
                width = 180.dp
            )

            MasterButton(
                type = ButtonType.WHITE,
                onClick = { /*todo*/ },
                modifier = Modifier,
                icon = R.drawable.editing,
                text = "EDIT",
                width = 180.dp
            )
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
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        MasterButton(
            type = ButtonType.RED,
            onClick = goToPickerScreen,
            modifier = Modifier,
            icon = R.drawable.dice,
            text = "DRAW",
            width = 180.dp
        )

        MasterButton(
            type = ButtonType.WHITE,
            onClick = { TODO() },
            modifier = Modifier,
            icon = null,
            text = "FAVOURITES",
            width = 180.dp,
        )
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

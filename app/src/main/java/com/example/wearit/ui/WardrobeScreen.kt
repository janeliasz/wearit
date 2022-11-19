package com.example.wearit.ui

import androidx.compose.foundation.*
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.ViewDebug.IntToString
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.wearit.model.Category
import com.example.wearit.model.Item
import java.util.*

@Composable
fun WardrobeScreen(
    goToPickerScreen: () -> Unit,
    onCategoryChange: (category: Category) -> Unit,
    itemsOfCurrentCategory: List<Item>,
    addItem: (bitmap: Bitmap) -> Boolean
) {

    val listOfCategories = Category.values().asList()

    Scaffold(
        content = { innerPadding ->
            //giving padding to whole content (so it doesnt overlap with bottomBar
            WardrobePageContent(
                innerPadding = innerPadding,
                onCategoryChange = onCategoryChange,
                listOfCategories = listOfCategories,
                itemsOfCurrentCategory = itemsOfCurrentCategory,
                addItem = addItem
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
    itemsOfCurrentCategory: List<Item>,
    addItem: (bitmap: Bitmap) -> Boolean
) {
    Box(modifier = Modifier.padding(innerPadding)) {
        Column() {
            WardrobeNavigationSection(
                addItem = addItem
            )
            WardrobeClothesListSection(
                onCategoryChange = onCategoryChange,
                listOfCategories = listOfCategories,
                itemsOfCurrentCategory = itemsOfCurrentCategory
            )
        }
    }
}

@Composable
fun WardrobeClothesListSection(
    onCategoryChange: (category: Category) -> Unit,
    listOfCategories: List<Category>,
    itemsOfCurrentCategory: List<Item>

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
            itemsOfCurrentCategory = itemsOfCurrentCategory
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
    itemsOfCurrentCategory: List<Item>
) {
    Box(
        Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopCenter

    ) {
        LazyVerticalGrid(
            cells = GridCells.Fixed(2)
        )
        {
            items(itemsOfCurrentCategory) { item ->
                SingleClothItem(item)
            }
        }
    }
}

@Composable
fun SingleClothItem(
    item: Item
) {
    Column(
        Modifier
            .padding(3.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Image(
            painter = painterResource(id = item.photoId),
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
    addItem: (bitmap: Bitmap) -> Boolean
) {
    val contentResolver = LocalContext.current.contentResolver
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            val bitmap = getBitmap(contentResolver, uri)
            if (bitmap != null) {
                addItem(bitmap)
            }
        }
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = { goToPickerScreen() }) {
            Text(text = "DRAW")
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

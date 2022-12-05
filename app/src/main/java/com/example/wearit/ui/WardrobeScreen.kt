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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.wearit.model.Category
import com.example.wearit.model.Item
import com.slowmac.autobackgroundremover.BackgroundRemover
import com.slowmac.autobackgroundremover.OnBackgroundChangeListener
import java.util.*

@Composable
fun WardrobeScreen(
    goToPickerScreen: () -> Unit,
    onCategoryChange: (category: Category) -> Unit,
    itemsOfCurrentCategory: List<Item>?,
    saveItem: (bitmap: Bitmap) -> Unit,
    getItemPhotoByPhotoFilename: (itemId: String) -> Bitmap,
    setActiveInactive: (item: Item) -> Unit,
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
                setActiveInactive = setActiveInactive
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
) {
    var isAddItemDialogOpen by remember { mutableStateOf(false) }
    var originalPhoto: Bitmap? by remember { mutableStateOf(null) }
    var noBgPhoto: Bitmap? by remember { mutableStateOf(null) }

    val contentResolver = LocalContext.current.contentResolver
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            val galleryPhoto = getBitmap(contentResolver, uri)
            if (galleryPhoto != null) {
                originalPhoto = galleryPhoto

                BackgroundRemover.bitmapForProcessing(
                    galleryPhoto,
                    false,
                    object: OnBackgroundChangeListener {
                        override fun onSuccess(bitmap: Bitmap) {
                            noBgPhoto = bitmap
                        }

                        override fun onFailed(exception: Exception) {
                            exception.printStackTrace()
                        }
                    }
                )

                isAddItemDialogOpen = true
            }
        }
    )

    if (isAddItemDialogOpen) {
        AddItemDialog(
            closeDialog = { isAddItemDialogOpen = false },
            originalPhoto = originalPhoto,
            noBgPhoto = noBgPhoto,
            saveItem = saveItem
        )
    }

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
fun AddItemDialog(
    closeDialog: () -> Unit,
    originalPhoto: Bitmap?,
    noBgPhoto: Bitmap?,
    saveItem: (bitmap: Bitmap) -> Unit
) {
    var showOriginal by remember { mutableStateOf(true) }
    
    Dialog(
        onDismissRequest = closeDialog
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (showOriginal && originalPhoto != null) {
                    Image(
                        bitmap = originalPhoto.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        contentScale = ContentScale.FillWidth
                    )
                }
                else if (!showOriginal && noBgPhoto != null) {
                    Image(
                        bitmap = noBgPhoto.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        contentScale = ContentScale.FillWidth
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp, 0.dp, 20.dp, 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = closeDialog) {
                        Text(text = "Cancel")
                    }

                    Button(
                        onClick = { showOriginal = !showOriginal },
                        enabled = noBgPhoto != null
                    ) {
                        Text(text = if (showOriginal) "Hide BG" else "Show BG")
                    }

                    Button(
                        onClick = {
                            saveItem(if (showOriginal) originalPhoto!! else noBgPhoto!!)
                            closeDialog()
                        },
                        enabled = showOriginal && originalPhoto != null || !showOriginal && noBgPhoto != null
                    ) {
                        Text(text = "Save")
                    }
                }
                
            }
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

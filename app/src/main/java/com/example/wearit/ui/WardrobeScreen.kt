package com.example.wearit.ui

import VerifyTick
import androidx.compose.foundation.*
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.wearit.R
import com.example.wearit.components.ButtonType
import com.example.wearit.components.MasterButton
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

            items(listOfCategories) { category ->

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
            .fillMaxWidth()
            .padding(10.dp, 0.dp),
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


    val itemOpacity: Float by animateFloatAsState(
        targetValue = if (item.isActive) 1f else 0.6f,
        animationSpec = tween(
            durationMillis = 500,
            easing = LinearEasing,
        )
    )
    val tickOpacity: Float by animateFloatAsState(
        targetValue = if (item.isActive) 1f else 0f,
        animationSpec = tween(
            durationMillis = 500,
            easing = LinearEasing,
        )
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(0.dp,5.dp)
    ) {

        VerifyTick(
            modifier = Modifier
                .align(Alignment.TopEnd),
            tickOpacity = tickOpacity,
            size = 30.dp
        )
        Box(modifier = Modifier
            .clip(shape = RoundedCornerShape(50.dp))
        ) {
            Column(
                Modifier
                    .clickable { setActiveInactive(item) }
                    .border(
                        width = 5.dp,
                        color = MaterialTheme.colors.primary.copy(alpha = itemOpacity),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .alpha(itemOpacity),
                horizontalAlignment = Alignment.CenterHorizontally,


                ) {

                Image(
                    bitmap = getItemPhotoByPhotoFilename(item.photoFilename).asImageBitmap(),
                    contentDescription = item.name,
                    modifier = Modifier
                        .size(120.dp)
                        .padding(5.dp, 10.dp, 5.dp, 0.dp)
                        .alpha(itemOpacity)
                )
                Text(
                    text = item.name, textAlign = TextAlign.Center, modifier = Modifier
                        .padding(5.dp, 0.dp, 5.dp, 10.dp)

                )
            }
        }
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

    Box(){
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
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(10.dp),
                icon = R.drawable.camera,
                text = "ADD",
            )

            MasterButton(
                type = ButtonType.WHITE,
                onClick = { /*todo*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(10.dp),
                icon = R.drawable.editing,
                text = "EDIT",
            )
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
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (showOriginal && originalPhoto != null) {
                    Image(
                        bitmap = originalPhoto.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(20.dp, 20.dp, 20.dp, 0.dp)
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
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MasterButton(
                        type = ButtonType.RED,
                        onClick = closeDialog,
                        text = "Cancel",
                        icon = null,
                        modifier = Modifier
                    )
                    MasterButton(
                        type = ButtonType.WHITE,
                        onClick = { showOriginal = !showOriginal },
                        text = if (showOriginal) "Hide BG" else "Show BG",
                        icon = null,
                        modifier = Modifier
                    )

                    MasterButton(
                        type = ButtonType.RED,
                        onClick = {
                            saveItem(if (showOriginal) originalPhoto!! else noBgPhoto!!)
                            closeDialog()
                        },
                        text = "Save",
                        icon = null,
                        modifier = Modifier
                    )
                }
                
            }
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
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(10.dp),
            icon = R.drawable.dice,
            text = "DRAW",
        )

        MasterButton(
            type = ButtonType.WHITE,
            onClick = { TODO() },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(10.dp),
            icon = null,
            text = "FAVOURITES",
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

package com.example.wearit.ui

import ImageIcon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
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
    deleteItem: (item: Item) -> Unit,
    goToSingleItem: (itemId: Int) -> Unit


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
                currentCategory = currentCategory,
                deleteItem = deleteItem,
                goToSingleItem = goToSingleItem
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
    currentCategory: Category,
    deleteItem: (item: Item) -> Unit,
    goToSingleItem: (itemId: Int) -> Unit

    ) {
    var editing: Boolean by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(innerPadding)) {
        Column() {
            WardrobeNavigationSection(
                saveItem = saveItem,
                editing = editing,
                toggleEdit = {editing = !editing}
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
                deleteItem = deleteItem,
                editing = editing,
                goToSingleItem = goToSingleItem,
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
    currentCategory: Category,
    deleteItem: (item: Item) -> Unit,
    editing: Boolean,
    goToSingleItem: (itemId: Int) -> Unit



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
            setActiveInactive = setActiveInactive,
            deleteItem = deleteItem,
            editing = editing,
            goToSingleItem = goToSingleItem,
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
    deleteItem: (item: Item) -> Unit,
    editing: Boolean,
    goToSingleItem: (itemId: Int) -> Unit


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
                        setActiveInactive = setActiveInactive,
                        deleteItem = deleteItem,
                        editing = editing,
                        goToSingleItem = goToSingleItem,

                    )
                }
            }
        } else {
            Text(text = "No items in category")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SingleClothItem(
    item: Item,
    getItemPhotoByPhotoFilename: (itemId: String) -> Bitmap,
    setActiveInactive: (item: Item) -> Unit,
    deleteItem: (item: Item) -> Unit,
    editing: Boolean,
    goToSingleItem: (itemId: Int) -> Unit

    ) {


    val itemOpacity: Float by animateFloatAsState(
        targetValue = if (item.isActive || editing) 1f else 0.6f,
        animationSpec = tween(
            durationMillis = 500,
            easing = LinearEasing,
        )
    )
    val tickOpacity: Float by animateFloatAsState(
        targetValue = if (item.isActive && !editing) 1f else 0f,
        animationSpec = tween(
            durationMillis = 500,
            easing = LinearEasing,
        )
    )
    val deleteOpacity: Float by animateFloatAsState(
        targetValue = if (editing) 1f else 0f,
        animationSpec = tween(
            durationMillis = 500,
            easing = LinearEasing,
        )
    )
    var isDeleteItemDialogOpen by remember { mutableStateOf(false) }

    if (isDeleteItemDialogOpen) {
        DeleteItemDialog(
            closeDialog = { isDeleteItemDialogOpen = false },
            deleteItem = deleteItem,
            item = item,
        )
    }



    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(0.dp,5.dp)
    ) {

        ImageIcon(
            modifier = Modifier
                .align(Alignment.TopEnd),
            tickOpacity = tickOpacity,
            size = 30.dp,
            icon = R.drawable.checkinside,
            onClick = {}
        )

        ImageIcon(
            modifier = Modifier
                .align(Alignment.TopEnd),
            tickOpacity = deleteOpacity,
            size = 30.dp,
            icon = R.drawable.close,
            onClick = { if(editing) isDeleteItemDialogOpen = !isDeleteItemDialogOpen }//deleteItem(item) }
        )

        Box(modifier = Modifier
            .clip(shape = RoundedCornerShape(50.dp)),
        ) {
            Column(
                Modifier
                    .height(150.dp)
                    .combinedClickable (
                        onClick = {
                            setActiveInactive(item)
                        },
                        onLongClick = {
                            goToSingleItem(item.id)
                        },
                    )
                    .border(
                        width = 5.dp,
                        color = MaterialTheme.colors.primary.copy(alpha = itemOpacity),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .alpha(itemOpacity),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center


                ) {

                Image(
                    bitmap = getItemPhotoByPhotoFilename(item.photoFilename).asImageBitmap(),
                    contentDescription = item.name,
                    modifier = Modifier
                        .size(120.dp)
                        .alpha(itemOpacity)
                )
//                Text(
//                    text = item.name, textAlign = TextAlign.Center, modifier = Modifier
//                        .padding(5.dp, 0.dp, 5.dp, 10.dp)
//
//                )
            }
        }
    }
}
@Composable
fun WardrobeNavigationSection(
    saveItem: (bitmap: Bitmap) -> Unit,
    editing: Boolean,
    toggleEdit: () -> Unit,
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
                .height(120.dp)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MasterButton(
                onClick = { imagePicker.launch("image/*") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                icon = R.drawable.camera,
                text = "ADD",
            )

            MasterButton(
                type = ButtonType.WHITE,
                onClick = { toggleEdit() },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                icon = R.drawable.editing,
                text = if(!editing) "EDIT" else "DONE",
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
                Image(
                    bitmap = if (showOriginal) originalPhoto!!.asImageBitmap() else noBgPhoto!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(20.dp, 20.dp, 20.dp, 0.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MasterButton(
                        type = ButtonType.WHITE,
                        onClick = { showOriginal = !showOriginal },
                        icon = if (showOriginal) R.drawable.scissors else R.drawable.undo_arrow,
                        enabled = noBgPhoto != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )

                    MasterButton(
                        onClick = {
                            saveItem(if (showOriginal) originalPhoto!! else noBgPhoto!!)
                            closeDialog()
                        },
                        icon = R.drawable.diskette,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }
                
            }
        }
    }
}


@Composable
fun DeleteItemDialog(
    closeDialog: () -> Unit,
    deleteItem: (item: Item) -> Unit,
    item: Item,
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
                Text(text = "CONFIRM DELETE:",
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
                        onClick = { deleteItem(item) },
                        text = "DELETE",
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
            onClick = { TODO() },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
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

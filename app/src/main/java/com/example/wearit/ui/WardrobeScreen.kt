package com.example.wearit.ui

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wearit.R
import com.example.wearit.model.Category
import com.example.wearit.ui.theme.Purple500
import java.util.*


@Composable
fun WardrobeScreen(
    currentCategory: Category,
    goToPickerScreen: () -> Unit,
    onCategoryChange: (category:Category) -> Unit,
    addItem: (bitmap: Bitmap) -> Boolean
) {
    val listOfCategories = (Category.values())

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

    Column {
        Button(onClick = { imagePicker.launch("image/*") }) {
            Text(text = "Add")
        }
        
        Text(text = "Current category is " + currentCategory.name)

        LazyColumn {
            items(listOfCategories) { category ->
                Button(onClick = { onCategoryChange(category) }) {
                    Text(text = "Go to $category")
                }
            }
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
    } catch (e: Exception){
        e.printStackTrace()
        null
    }
}
package com.example.wearit.ui

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.wearit.model.Category

@Composable
fun WardrobeScreen(
    currentCategory: Category,
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
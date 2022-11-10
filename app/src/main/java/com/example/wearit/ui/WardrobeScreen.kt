package com.example.wearit.ui

import android.util.Log
import android.view.ViewDebug.IntToString
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

) {
    val fonts = FontFamily(
        Font(R.font.reemkufi_bold, FontWeight.Bold)
    )
    val listOfCategories = (Category.values())


    Column {
        Text(text = "Current category is " + currentCategory.name)

        LazyColumn {
            items(listOfCategories) { category ->
                Button(onClick = { onCategoryChange(category) }) {
                    Text(text = "Go to $category")
                }
            }
        }
    }
    //draw button
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ){
        ExtendedFloatingActionButton(
            modifier = Modifier
                .padding(all = 40.dp)
                .fillMaxWidth(),
            onClick = goToPickerScreen,
            backgroundColor = Color.Red,
            contentColor = Color.White,
            text = { Text("DRAW",
                modifier = Modifier.padding(all = 8.dp),
                fontFamily = fonts,
                fontSize = 30.sp,

//                style = MaterialTheme.typography.button,
            )}
        )
    }
}
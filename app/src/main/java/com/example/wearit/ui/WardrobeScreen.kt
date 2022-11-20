package com.example.wearit.ui

import android.media.Image
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.layout.ColumnScopeInstance.align
//import androidx.compose.foundation.layout.ColumnScopeInstance.align
//import androidx.compose.foundation.layout.ColumnScopeInstance.align
//import androidx.compose.foundation.layout.ColumnScopeInstance.align
//import androidx.compose.foundation.layout.ColumnScopeInstance.align
//import androidx.compose.foundation.layout.ColumnScopeInstance.align
//import androidx.compose.foundation.layout.ColumnScopeInstance.align
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.wearit.model.Category
import com.example.wearit.model.Item
import java.util.*
import kotlin.collections.ArrayList

@Composable
fun WardrobeScreen(
    currentCategory: Category,
    goToPickerScreen: () -> Unit,
    onCategoryChange: (category: Category) -> Unit,
    itemsOfCurrentCategory: List<Item>?,

    ) {

    val listOfCategories = (Category.values())

    val array = ArrayList<Item>()
    if (itemsOfCurrentCategory != null) {
        for (item in itemsOfCurrentCategory) {
            array.add(item)
        }
    }

    Scaffold(
        content = { innerPadding ->
            //giving padding to whole content (so it doesnt overlap with bottomBar
            WardrobePageContent(
                innerPadding = innerPadding,
                onCategoryChange = onCategoryChange,
                listOfCategories=listOfCategories,
                itemsOfCurrentCategory = array
                )
        },
            bottomBar = {
                BottomBarSpace()
            }
    )
}

@Composable
fun WardrobePageContent(
    innerPadding: PaddingValues,
    onCategoryChange: (category: Category) -> Unit,
    listOfCategories: Array<Category>,
    itemsOfCurrentCategory: ArrayList<Item>
){
    Box(modifier = Modifier.padding(innerPadding)) {
        Column() {
            WardrobeNavigationSection()
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
    listOfCategories: Array<Category>,
    itemsOfCurrentCategory: ArrayList<Item>

){
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
    listOfCategories: Array<Category>,
){
    Column(
        modifier = Modifier
            .background(Color.Green)
    ) {

        LazyColumn {
            items(listOfCategories) { category ->
                Button(onClick = { onCategoryChange(category) }) {
                    Text(text = "Go to $category")
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WardrobeListOfItemsFromCurrentCategory(
    itemsOfCurrentCategory: ArrayList<Item>
){
    Box(
        Modifier
            .background(Color.Yellow)
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
){
    Column(
        Modifier
            .background(Color.Green)
            .padding(3.dp)
            .border(
                width = 3.dp,
                color = Color.Red,
                shape = RoundedCornerShape(50.dp)
            ),

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
                .border(width = 3.dp, color = Color.Red)
                .padding(5.dp, 0.dp, 5.dp, 10.dp)

        )
    }
}

@Composable
fun WardrobeNavigationSection(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.Red)
    ) {
        Button(onClick = { /*TODO*/ }) {
            Text(text = "ADD")
        }
        Button(onClick = { /*TODO*/ }) {
            Text(text = "EDIT")
        }
    }
}


@Composable
fun BottomBarSpace(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .border(
                width = 3.dp,
                color = Color.Red,
            ),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = { /*TODO*/ }) {
            Text(text = "DRAW")
        }
    }
}

package com.example.wearit.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.wearit.model.Category
import com.example.wearit.model.Item
import java.util.*

@Composable
fun WardrobeScreen(
    goToPickerScreen: () -> Unit,
    onCategoryChange: (category: Category) -> Unit,
    itemsOfCurrentCategory: List<Item>?,

    ) {

    val listOfCategories = Category.values().asList()


    Scaffold(
        content = { innerPadding ->
            //giving padding to whole content (so it doesnt overlap with bottomBar
            WardrobePageContent(
                innerPadding = innerPadding,
                onCategoryChange = onCategoryChange,
                listOfCategories=listOfCategories,
                itemsOfCurrentCategory = itemsOfCurrentCategory!!
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
    itemsOfCurrentCategory: List<Item>
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
    listOfCategories: List<Category>,
    itemsOfCurrentCategory: List<Item>

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
    listOfCategories: List<Category>,
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
    itemsOfCurrentCategory: List<Item>
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
fun BottomBarSpace(
    goToPickerScreen: () -> Unit,

    ){
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
        Button(onClick = { goToPickerScreen() }) {
            Text(text = "DRAW")
        }
    }
}

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


@OptIn(ExperimentalFoundationApi::class)
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
//        topBar = { TopAppBar(title = {Text("TopAppBar")},backgroundColor = Color.LightGray)  },
        content = {
                Column() {
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
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
//                    .width(Space)
                    .background(Color.Green)
            ) {
//                Text(text = "Current category is " + currentCategory.name)

                LazyColumn {
                    items(listOfCategories) { category ->
                        Button(onClick = { onCategoryChange(category) }) {
                            Text(text = "Go to $category")
                        }
                    }
                }
            }
            Box(
                Modifier
                    .background(Color.Yellow)
                    .fillMaxWidth(),
//                    .fillMaxHeight(),
                contentAlignment = Alignment.TopCenter

            ) {
                LazyVerticalGrid(
                    cells = GridCells.Fixed(1)
                )
//                LazyColumn()
                {
                    items(array) { item ->

                        Column(
                            Modifier.background(Color.Green)
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
                                modifier = Modifier.size(100.dp)
                                    .padding(5.dp, 10.dp, 5.dp, 0.dp)

                            )
                            Text(
                                text = item.name, textAlign = TextAlign.Center, modifier = Modifier
                                    .border(width = 3.dp, color = Color.Red)
                                    .padding(5.dp, 0.dp, 5.dp, 10.dp)

                            )
                        }
                    }
                }
            }
        }

    }
},
            bottomBar = {
                BottomAppBar(cutoutShape = CircleShape) {
                    IconButton(
                        onClick = {
                             }

                    ) {
                        Icon(Icons.Filled.Menu,"")
                    }
                }

//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(100.dp),
////                        .background(Color(0x2FDFDFDF)),
//                    contentAlignment = Alignment.BottomCenter
//
//
//                ) {
//                    Button(onClick = { /*TODO*/ }) {
//                        Text(text = "DRAW")
//                    }
//                }
            }

            )
        }


package com.example.wearit.model

import com.example.wearit.R

val fakeItemsData: Map<Category, List<Item>> = mapOf(
    Category.Headgear to listOf(
        Item(
            id = "1",
            name = "hat",
            photoId = R.drawable.hat,
            category = Category.Headgear,
            isActive = true
        ),
        Item(
            id = "2",
            name = "hat2",
            photoId = R.drawable.hat2,
            category = Category.Headgear,
            isActive = true
        ),
        Item(
            id = "3",
            name = "hat3",
            photoId = R.drawable.hat3,
            category = Category.Headgear,
            isActive = true
        ),
        Item(
            id = "4",
            name = "hat4",
            photoId = R.drawable.hat4,
            category = Category.Headgear,
            isActive = true
        )
    ),
    Category.Blouse to listOf(
        Item(
            id = "5",
            name = "sweater",
            photoId = R.drawable.sweater,
            category = Category.Blouse,
            isActive = true
        )
    ),
    Category.Tshirt to listOf(
        Item(
            id = "6",
            name = "tshirt",
            photoId = R.drawable.tshirt,
            category = Category.Tshirt,
            isActive = true
        )
    ),
    Category.Trousers to listOf(
        Item(
            id = "7",
            name = "jeans",
            photoId = R.drawable.jeans,
            category = Category.Trousers,
            isActive = true
        ),
        Item(
            id = "8",
            name = "cargos",
            photoId = R.drawable.cargos,
            category = Category.Trousers,
            isActive = true
        )
    ),
    Category.Boots to listOf(
        Item(
            id = "9",
            name = "shoes",
            photoId = R.drawable.shoes,
            category = Category.Boots,
            isActive = true
        )
    )
)
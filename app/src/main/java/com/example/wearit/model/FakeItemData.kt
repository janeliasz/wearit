package com.example.wearit.model

import com.example.wearit.R

val fakeItems = listOf<Item>(
        Item(
                id = "1",
                name = "hat",
                photoId = R.drawable.hat,
                category = Category.Headgear
        ),
        Item(
                id = "2",
                name = "sweater",
                photoId = R.drawable.sweater,
                category = Category.Blouse
        ),
        Item(
                id = "3",
                name = "jeans",
                photoId = R.drawable.jeans,
                category = Category.Trousers
        ),
        Item(
                id = "4",
                name = "shoes",
                photoId = R.drawable.shoes,
                category = Category.Boots
        )
)
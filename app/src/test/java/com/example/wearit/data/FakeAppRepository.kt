package com.example.wearit.data

import com.example.wearit.model.Category
import com.example.wearit.model.Item
import com.example.wearit.model.Outfit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

val fakeItems = mutableListOf<Item>(
    Item(
        id = 1,
        name = "item-1",
        photoFilename = "item-1.png",
        category = Category.Headgear
    ),
    Item(
        id = 2,
        name = "item-2",
        photoFilename = "item-2.png",
        category = Category.Headgear
    ),
    Item(
        id = 3,
        name = "item-3",
        photoFilename = "item-3.png",
        category = Category.Tshirt
    ),
    Item(
        id = 4,
        name = "item-4",
        photoFilename = "item-4.png",
        category = Category.Shorts
    )
)

val fakeOutfits = mutableListOf<Outfit>(
    Outfit(
        id = 1,
        itemsInOutfit = listOf(1, 3, 4)
    ),
    Outfit(
        id = 2,
        itemsInOutfit = listOf(3, 4)
    ),
    Outfit(
        id = 3,
        itemsInOutfit = listOf(2, 4)
    )

)

class FakeAppRepository(private val fakeItems: MutableList<Item>, private val fakeOutfits: MutableList<Outfit>) : IAppRepository {
    override val getAllItems: Flow<List<Item>> = flow {
        emit(fakeItems)
    }

    override val getAllOutfits: Flow<List<Outfit>> = flow {
        emit(fakeOutfits)
    }

    override suspend fun addItem(item: Item) {
        fakeItems.add(item)
    }

    override suspend fun updateItem(item: Item) {
        val index = fakeItems.indexOfFirst { it.id == item.id }
        fakeItems[index] = item
    }

    override suspend fun deleteItem(item: Item) {
        fakeItems.remove(item)
    }

    override suspend fun addOutfit(outfit: Outfit) {
        fakeOutfits.add(outfit)
    }

    override suspend fun updateOutfit(outfit: Outfit) {
        val index = fakeOutfits.indexOfFirst { it.id == outfit.id }
        fakeOutfits[index] = outfit
    }

    override suspend fun deleteOutfit(outfit: Outfit) {
        fakeOutfits.remove(outfit)
    }
}
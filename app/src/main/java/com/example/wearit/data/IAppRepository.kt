package com.example.wearit.data

import com.example.wearit.model.Item
import com.example.wearit.model.Outfit
import kotlinx.coroutines.flow.Flow

interface IAppRepository {
    val getAllItems: Flow<List<Item>>
    val getAllOutfits: Flow<List<Outfit>>

    suspend fun addItem(item: Item)

    suspend fun updateItem(item: Item)

    suspend fun deleteItem(item: Item)

    suspend fun addOutfit(outfit: Outfit)

    suspend fun updateOutfit(outfit: Outfit)

    suspend fun deleteOutfit(outfit: Outfit)
}
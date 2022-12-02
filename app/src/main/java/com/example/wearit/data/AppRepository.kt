package com.example.wearit.data

import com.example.wearit.model.Item
import com.example.wearit.model.Outfit
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow

class AppRepository(private val itemDao: ItemDao,private val outfitDao: OutfitDao) {

    val getAllItems: Flow<List<Item>> = itemDao.getAll()

    suspend fun addItem(item: Item) {
        itemDao.insert(item)
    }

    suspend fun updateItem(item: Item) {
        itemDao.update(item)
    }

    suspend fun deleteItem(item: Item) {
        itemDao.delete(item)
    }



    val getAllOutfits: Flow<List<Outfit>> = outfitDao.getAll()

    suspend fun addOutfit(outfit: Outfit) {
        outfitDao.insert(outfit)
    }

    suspend fun updateOutfit(outfit: Outfit) {
        outfitDao.update(outfit)
    }

    suspend fun deleteOutfit(outfit: Outfit) {
        outfitDao.delete(outfit)
    }

    fun findOutfit(selected: List<Int>): Int? {
        return outfitDao.getOutfit(Gson().toJson(selected))
    }
}
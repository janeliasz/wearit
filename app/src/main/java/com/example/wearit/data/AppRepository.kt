package com.example.wearit.data

import com.example.wearit.model.Item
import com.example.wearit.model.Outfit
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppRepository @Inject constructor(private val itemDao: ItemDao, private val outfitDao: OutfitDao) :
    IAppRepository {

    override val getAllItems: Flow<List<Item>> = itemDao.getAll()

    override suspend fun addItem(item: Item) {
        itemDao.insert(item)
    }

    override suspend fun updateItem(item: Item) {
        itemDao.update(item)
    }

    override suspend fun deleteItem(item: Item) {
        itemDao.delete(item)
    }



    override val getAllOutfits: Flow<List<Outfit>> = outfitDao.getAll()

    override suspend fun addOutfit(outfit: Outfit) {
        outfitDao.insert(outfit)
    }

    override suspend fun updateOutfit(outfit: Outfit) {
        outfitDao.update(outfit)
    }

    override suspend fun deleteOutfit(outfit: Outfit) {
        outfitDao.delete(outfit)
    }
}
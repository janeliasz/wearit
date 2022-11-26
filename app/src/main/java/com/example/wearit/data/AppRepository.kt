package com.example.wearit.data

import com.example.wearit.model.Item
import kotlinx.coroutines.flow.Flow

class AppRepository(private val itemDao: ItemDao) {

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
}
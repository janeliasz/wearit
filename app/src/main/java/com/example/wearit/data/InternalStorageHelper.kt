package com.example.wearit.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.wearit.model.Category
import com.example.wearit.model.Item
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.IOException
import java.util.UUID

class InternalStorageHelper(private val context: Context) {
    fun savePhoto(bitmap: Bitmap): String {
        return try {
            val filename = UUID.randomUUID().toString() + ".jpg"
            context.openFileOutput(filename, Context.MODE_PRIVATE).use { stream ->
                if(!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                    throw IOException("Could not save photo.")
                }
            }
            filename
        } catch(e: IOException) {
            e.printStackTrace()
            ""
        }
    }

    fun loadPhotos(): Map<String, Bitmap> {
        val photosMap: MutableMap<String, Bitmap> = mutableMapOf()

        val files = context.filesDir.listFiles()
        files.filter { it.canRead() && it.isFile && it.name.endsWith(".jpg")}.forEach() {
            val bytes = it.readBytes()
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            photosMap[it.name] = bitmap
        }

        return photosMap
    }

    fun saveItem(item: Item): Boolean {
        return try {
            val json = Json.encodeToString<Item>(item)
            context.openFileOutput("item-" + item.id, Context.MODE_PRIVATE).use { stream ->
                stream.write(json.toByteArray())
            }
            true
        } catch(e: IOException) {
            e.printStackTrace()
            false
        }
    }

    fun loadItems(): List<Item> {
        val files = context.filesDir.listFiles()
        return files.filter { it.canRead() && it.isFile && it.name.startsWith("item-")}.map {
            Json.decodeFromStream<Item>(it.inputStream())
        }
    }

    fun getItemsMap(): Map<Category, List<Item>> {
        val itemsMap = mutableMapOf<Category, MutableList<Item>>()

        val itemList = loadItems()

        itemList.forEach { item ->
            if (itemsMap.containsKey(item.category)) {
                itemsMap.getValue(item.category).add(item)
            }
            else {
                itemsMap[item.category] = mutableListOf(item)
            }
        }

        return itemsMap
    }

    fun deleteFile(filename: String): Boolean {
        return try {
            context.deleteFile(filename)
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
}
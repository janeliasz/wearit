package com.example.wearit.data

import android.content.Context
import androidx.room.*
import com.example.wearit.model.Item
import com.example.wearit.model.Outfit
import com.google.gson.Gson

@Database(entities = [Item::class, Outfit::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao
    abstract fun outfitDao(): OutfitDao

}

class Converters {
    @TypeConverter
    fun listToJson(list: List<Int>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun jsonToList(value: String): List<Int> {
        return Gson().fromJson(value, Array<Int>::class.java).toList()
    }
}
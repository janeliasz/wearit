package com.example.wearit.data

import androidx.room.*
import com.example.wearit.model.Outfit
import kotlinx.coroutines.flow.Flow

@Dao
interface OutfitDao {
    @Query("SELECT * FROM Outfit")
    fun getAll(): Flow<List<Outfit>>

    @Insert
    suspend fun insert(outfit: Outfit)

    @Update
    suspend fun update(outfit: Outfit)

    @Delete
    suspend fun delete(outfit: Outfit)
}
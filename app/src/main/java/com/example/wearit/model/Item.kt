package com.example.wearit.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var name: String,
    val photoFilename: String,
    val category: Category,
    var isActive: Boolean = true
)
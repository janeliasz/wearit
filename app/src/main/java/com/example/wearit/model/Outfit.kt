package com.example.wearit.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Outfit(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var itemsInOutfit : List<Int>
)

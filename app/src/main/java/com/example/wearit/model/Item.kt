package com.example.wearit.model

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: String,
    var name: String,
    val photoId: Int = -1, // temporary change, photoId is going to be removed
    val photoFilename: String = "", // default value will be removed, once fake data is removed
    val category: Category,
    var isActive: Boolean = true
)
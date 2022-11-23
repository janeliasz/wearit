package com.example.wearit.model

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: String,
    var name: String,
    val photoFilename: String,
    val category: Category,
    var isActive: Boolean = true
)
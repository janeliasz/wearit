package com.example.wearit.model

class Item(
    val id: String,
    var name: String,
    val photoId: Int,
    val category: Category,
    var isActive: Boolean = true
) {
    fun toggleActive(){
        isActive = !isActive
    }
}
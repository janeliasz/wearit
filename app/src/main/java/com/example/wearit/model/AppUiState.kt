package com.example.wearit.model

data class AppUiState(
    val items: Map<Category, List<Item>>,
    val currentSelection: List<Item>,
    val currentCategory: Category = Category.Headgear,
)

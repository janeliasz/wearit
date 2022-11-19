package com.example.wearit.model

data class AppUiState(
    val currentCategory: Category = Category.Headgear,
    val currentSelection: List<Item>,
    val items: Map<Category, List<Item>>,
)

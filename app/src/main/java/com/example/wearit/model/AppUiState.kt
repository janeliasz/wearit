package com.example.wearit.model

data class AppUiState(
    val items: Map<Category, List<Item>>,
    val currentSelection: List<String>,
    val currentCategory: Category = Category.Headgear,
)

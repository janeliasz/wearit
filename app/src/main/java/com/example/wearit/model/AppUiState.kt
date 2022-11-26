package com.example.wearit.model

data class AppUiState(
    val currentSelection: List<Int>,
    val currentCategory: Category = Category.Headgear,
)

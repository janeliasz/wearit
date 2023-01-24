package com.example.wearit.di

import com.example.wearit.model.Category

data class AppUiState(
    val currentSelection: List<Int>,
    val currentCategory: Category = Category.Headgear,
)

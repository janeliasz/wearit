package com.example.wearit.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.example.wearit.model.Category

@Composable
fun WardrobeScreen(
    currentCategory: Category
) {
    Text(text = "Current category is " + currentCategory.name)
}
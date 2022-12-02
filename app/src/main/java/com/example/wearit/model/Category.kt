package com.example.wearit.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.wearit.R

enum class Category(val icon: Int) {
    Headgear(R.drawable.hat),
    Coat(R.drawable.coat),
    Blouse(R.drawable.hoodie),
    Tshirt(R.drawable.tshirt),
    Trousers(R.drawable.trouser),
    Shorts(R.drawable.shorts),
    Boots(R.drawable.boots),
}
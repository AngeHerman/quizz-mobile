package fr.uparis.nzaba.projetmobile2023.data

import androidx.compose.ui.graphics.vector.ImageVector

data class DrawItem<T : Any>(
    val icon: ImageVector,
    val label: String,
    val activityClass: Class<out T>
)

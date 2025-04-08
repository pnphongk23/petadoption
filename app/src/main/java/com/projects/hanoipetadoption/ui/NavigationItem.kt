package com.projects.hanoipetadoption.ui

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val titleRes: Int
)
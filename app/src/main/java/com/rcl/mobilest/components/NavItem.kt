package com.rcl.mobilest.components

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val configuration: RootComponent.TopLevelConfiguration,
    val icon: ImageVector,
    @StringRes val textId: Int,
)

package com.softprodigy.ballerapp

import androidx.compose.ui.graphics.Color
import com.softprodigy.ballerapp.ui.features.components.TopBar
import com.softprodigy.ballerapp.ui.features.components.TopBarData

data class MainState(
    val topBar: TopBarData = TopBarData("", TopBar.EMPTY),
    val showAppBar: Boolean = false,
    val screen: Boolean = false,
    val color: Color? = null
)
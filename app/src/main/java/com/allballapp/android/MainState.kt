package com.allballapp.android

import androidx.compose.ui.graphics.Color
import com.allballapp.android.ui.features.components.TopBar
import com.allballapp.android.ui.features.components.TopBarData

data class MainState(
    val topBar: TopBarData = TopBarData("", TopBar.EMPTY),
    val showAppBar: Boolean = false,
    val color: Color? = null
)
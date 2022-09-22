package com.softprodigy.ballerapp

import androidx.compose.ui.graphics.Color
import com.softprodigy.ballerapp.ui.features.components.TopBarData

sealed class MainEvent {
    data class OnTopBarChanges(val showAppBar: Boolean, val topBarData: TopBarData) : MainEvent()
    data class OnScreenChanges(val screen: Boolean) : MainEvent()
    data class OnColorChanges(val screen: Color) : MainEvent()
}
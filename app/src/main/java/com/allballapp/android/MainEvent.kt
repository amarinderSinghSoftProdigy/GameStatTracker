package com.allballapp.android

import androidx.compose.ui.graphics.Color
import com.allballapp.android.ui.features.components.TopBarData

sealed class MainEvent {
    data class OnTopBarChanges(val showAppBar: Boolean, val topBarData: TopBarData) : MainEvent()
    data class OnShowTopBar(val showAppBar: Boolean) : MainEvent()
    data class OnColorChanges(val color: Color) : MainEvent()
}
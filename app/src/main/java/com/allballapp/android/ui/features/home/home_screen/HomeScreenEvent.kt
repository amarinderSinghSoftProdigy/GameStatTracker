package com.allballapp.android.ui.features.home.home_screen

import com.allballapp.android.ui.features.components.Environment

sealed class HomeScreenEvent {
    data class OnEnvUpdate(val env: String = Environment.DEVELOP.value) : HomeScreenEvent()
    data class OnSwapClick(val check: Boolean = false) : HomeScreenEvent()
    data class HideSwap(val check: Boolean = false) : HomeScreenEvent()
    data class OnSwapUpdate(val userId: String) : HomeScreenEvent()
    data class OnNetworkAvailable(val isAvailable: Boolean) : HomeScreenEvent()
}
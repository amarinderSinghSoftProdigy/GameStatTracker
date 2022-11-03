package com.allballapp.android.ui.features.home.home_screen

sealed class HomeScreenEvent {
    object OnSwapClick : HomeScreenEvent()
    data class HideSwap(val check: Boolean = false) : HomeScreenEvent()
    data class OnSwapUpdate(val userId: String) : HomeScreenEvent()
}
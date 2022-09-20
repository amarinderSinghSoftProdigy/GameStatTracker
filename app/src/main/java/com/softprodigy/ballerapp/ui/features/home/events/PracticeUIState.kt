package com.softprodigy.ballerapp.ui.features.home.events

import java.net.Inet4Address

data class PracticeUIState(
    val isLoading: Boolean = false,
    val selectedDate: String = "",
    val selectedArrivalTime: String = "",
    val selectedStartTime: String = "",
    val selectedEndTime: String = "",
    val selectedLocation: String = "",
    val selectedAddress: String = ""
)
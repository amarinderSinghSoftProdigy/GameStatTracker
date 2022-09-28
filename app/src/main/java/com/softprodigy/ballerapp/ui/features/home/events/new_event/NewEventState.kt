package com.softprodigy.ballerapp.ui.features.home.events.new_event

data class NewEventState(
    val isLoading: Boolean = false,
    val eventName: String = "",
    val eventType: String = "",
    val selectedDate: String = "",
    val selectedArrivalTime: String = "",
    val selectedStartTime: String = "",
    val selectedEndTime: String = "",
    val selectedLocation: String = "",
    val selectedAddress: String = ""
)
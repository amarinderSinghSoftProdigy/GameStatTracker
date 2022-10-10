package com.softprodigy.ballerapp.ui.features.home.events.new_event

import com.softprodigy.ballerapp.data.request.Address

data class NewEventState(
    val isLoading: Boolean = false,
    val showNotification: Boolean = false,
    val eventName: String = "",
    val eventType: String = "",
    val selectedDate: String = "",
    val selectedArrivalTime: String = "",
    val selectedStartTime: String = "",
    val selectedEndTime: String = "",
//    val selectedLocation: String = "",
    val selectedVenueName: String = "",
    val selectedAddress: Address = Address()
)
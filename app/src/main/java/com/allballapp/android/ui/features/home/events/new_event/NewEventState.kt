package com.allballapp.android.ui.features.home.events.new_event

import com.allballapp.android.data.request.Address

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
    val selectedAddress: Address = Address(),
    val pre_practice_prep: String = "",
    val savedDate: String = ""
)
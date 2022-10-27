package com.softprodigy.ballerapp.ui.features.home.events.new_event

import com.softprodigy.ballerapp.data.request.Address

sealed class NewEvEvent{
    data class OnEventNameChange(val eventName: String) : NewEvEvent()
    data class OnEventTypeChange(val eventType: String) : NewEvEvent()
    data class OnDateChanged(val date: String) : NewEvEvent()
    data class OnArrivalTimeChanged(val time: String) : NewEvEvent()
    data class OnStartTimeChanged(val time: String) : NewEvEvent()
    data class OnEndTimeChanged(val time: String) : NewEvEvent()
    data class OnLocationVenueChange(val venueName: String) : NewEvEvent()
    data class OnAddressChanged(val address: Address) : NewEvEvent()
    data class OnNotificationChange(val showNotification: Boolean) : NewEvEvent()
    data class OnPrePracticeChange(val prePracticePrep: String) : NewEvEvent()
    object OnSaveButtonClick : NewEvEvent()
}

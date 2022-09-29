package com.softprodigy.ballerapp.ui.features.home.events.new_event

sealed class NewEvEvent{
    data class OnEventNameChange(val eventName: String) : NewEvEvent()
    data class OnEventTypeChange(val eventType: String) : NewEvEvent()
    data class OnDateChanged(val date: String) : NewEvEvent()
    data class OnArrivalTimeChanged(val time: String) : NewEvEvent()
    data class OnStartTimeChanged(val time: String) : NewEvEvent()
    data class OnEndTimeChanged(val time: String) : NewEvEvent()
//    data class OnLocationChanged(val location: String) : NewEvEvent()
    data class OnAddressChanged(val address: String) : NewEvEvent()
    data class OnVenueChange(val venueName: String) : NewEvEvent()
    data class OnNotificationChange(val showNotification: Boolean) : NewEvEvent()
    object OnSaveButtonClick : NewEvEvent()
}

package com.softprodigy.ballerapp.ui.features.home.events.practice

sealed class PracticeUIEvent {

    data class OnDateChanged(val date: String) : PracticeUIEvent()
    data class OnArrivalTimeChanged(val time: String) : PracticeUIEvent()
    data class OnStartTimeChanged(val time: String) : PracticeUIEvent()
    data class OnEndTimeChanged(val time: String) : PracticeUIEvent()
    data class OnLocationChanged(val location: String) : PracticeUIEvent()
    data class OnAddressChanged(val address: String) : PracticeUIEvent()
}
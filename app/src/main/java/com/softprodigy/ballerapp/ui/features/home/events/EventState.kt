package com.softprodigy.ballerapp.ui.features.home.events


data class EventState(
    val currentEvents: ArrayList<EventsModel> = arrayListOf(),
    val pastEvents: ArrayList<EventsModel> = arrayListOf(),
    val leagues: ArrayList<Leagues> = arrayListOf(),
    val oppotuntities: ArrayList<Leagues> = arrayListOf(),
    val showGoingDialog: Boolean = false,
    val showDeclineDialog: Boolean = false,
    val showLoading: Boolean = false,
    val selectedEvent: EventsModel = EventsModel(),
)
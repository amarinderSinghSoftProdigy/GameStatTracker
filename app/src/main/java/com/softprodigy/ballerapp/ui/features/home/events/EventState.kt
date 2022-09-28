package com.softprodigy.ballerapp.ui.features.home.events


data class EventState(
    val isLoading: Boolean = false,
    val currentEvents: ArrayList<Events> = arrayListOf(),
    val pastEvents: ArrayList<Events> = arrayListOf(),
    val leagues: ArrayList<Leagues> = arrayListOf(),
    val oppotuntities: ArrayList<Leagues> = arrayListOf(),
    val showGoingDialog: Boolean = false,
    val showDeclineDialog: Boolean = false,
    val showLoading: Boolean = false,
    val selectedEvent: Events = Events(),
    val selectedEventId: String = "",
    val filterPreference: List<FilterPreference> = mutableListOf(),
    val opportunitiesList: List<OpportunitiesItem> = mutableListOf(),
    val opportunitiesDetail: OpportunitiesDetail = OpportunitiesDetail()
)
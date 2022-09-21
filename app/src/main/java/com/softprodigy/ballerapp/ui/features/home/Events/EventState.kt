package com.softprodigy.ballerapp.ui.features.home.Events

import com.softprodigy.ballerapp.ui.features.home.invitation.Invitation


data class EventState(
    val currentEvents: ArrayList<Events> = arrayListOf(),
    val pastEvents: ArrayList<Events> = arrayListOf(),
    val leagues: ArrayList<Leagues> = arrayListOf(),
    val oppotuntities: ArrayList<Leagues> = arrayListOf(),
    val showGoingDialog: Boolean = false,
    val showDeclineDialog: Boolean = false,
    val showLoading: Boolean = false,
    val selectedEvent: Events = Events(),
    )
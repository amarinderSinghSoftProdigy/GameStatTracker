package com.softprodigy.ballerapp.ui.features.home.events.schedule

data class EventScheduleState(
    val isLoading: Boolean = false,
    val leagueSchedules: List<LeagueScheduleModel> = listOf(),
)
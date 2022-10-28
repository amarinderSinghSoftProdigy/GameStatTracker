package com.allballapp.android.ui.features.home.event_kd.schedule

data class EventScheduleState(
    val isLoading: Boolean = false,
    val leagueSchedules: List<LeagueScheduleModel> = listOf(),
)
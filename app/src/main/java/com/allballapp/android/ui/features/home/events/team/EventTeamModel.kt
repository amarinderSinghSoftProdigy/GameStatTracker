package com.allballapp.android.ui.features.home.events.team

import com.allballapp.android.data.response.team.Team


data class EventTeamModel(
    val divisionName: String = "",
    val teams: List<Team> = listOf()
)

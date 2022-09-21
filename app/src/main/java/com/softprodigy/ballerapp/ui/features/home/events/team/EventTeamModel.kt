package com.softprodigy.ballerapp.ui.features.home.events.team

import com.softprodigy.ballerapp.data.response.team.Team


data class EventTeamModel(
    val divisionName: String = "",
    val teams: List<Team> = listOf()
)

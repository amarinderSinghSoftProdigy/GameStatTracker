package com.softprodigy.ballerapp.ui.features.home.teams

import com.softprodigy.ballerapp.data.response.Team


data class TeamUIState(
    val isLoading: Boolean = false,
    val teams: ArrayList<Team> = ArrayList(),
    val selectedTeam: Team = Team(),
    val showDialog: Boolean = false,
)

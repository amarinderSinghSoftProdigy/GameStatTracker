package com.softprodigy.ballerapp.ui.features.home.teams

import com.softprodigy.ballerapp.data.response.Standing
import com.softprodigy.ballerapp.data.response.Team


data class TeamUIState(
    val isLoading: Boolean = false,
    val teams: ArrayList<Team> = ArrayList(),
    val selectedTeam: Team? = null,
    val showDialog: Boolean = false,

    val standing: ArrayList<Standing> = ArrayList(),
    val selectedStanding: Standing? = null,

    )

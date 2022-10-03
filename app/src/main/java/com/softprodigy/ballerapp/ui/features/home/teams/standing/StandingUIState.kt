package com.softprodigy.ballerapp.ui.features.home.teams.standing

import com.softprodigy.ballerapp.data.response.AllTeams
import com.softprodigy.ballerapp.data.response.Categories
import com.softprodigy.ballerapp.data.response.Standing

data class StandingUIState(
    val isLoading: Boolean = false,
    val standing: List<Standing> = emptyList<Standing>(),
    val categories: List<Categories> = emptyList(),
    val selectedStanding:Standing=Standing()
    )
data class StandingLeagueDivisionUIState(
    val isLoading: Boolean = false,
    val allTeam: AllTeams = AllTeams(),
    val categories: List<Categories> = emptyList(),
    val selectedStanding:Standing=Standing()
    )
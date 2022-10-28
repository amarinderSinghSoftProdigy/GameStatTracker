package com.allballapp.android.ui.features.home.teams.standing

import com.allballapp.android.data.response.AllTeams
import com.allballapp.android.data.response.Categories
import com.allballapp.android.data.response.Standing

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
package com.softprodigy.ballerapp.ui.features.home.teams.standing

import com.softprodigy.ballerapp.data.response.Standing

data class StandingUIState(
    val isLoading: Boolean = false,
    val standing: List<Standing> = emptyList<Standing>(),
    val categories: List<String> = emptyList(),
    val selectedStanding:Standing=Standing()
    )
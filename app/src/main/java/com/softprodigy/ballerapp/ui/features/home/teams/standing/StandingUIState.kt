package com.softprodigy.ballerapp.ui.features.home.teams.standing

import com.softprodigy.ballerapp.data.response.Standing

data class StandingUIState(
    val isLoading: Boolean = false,
    val standing: MutableList<Standing> = mutableListOf(),
    val selectedStanding:Standing=Standing()
    )
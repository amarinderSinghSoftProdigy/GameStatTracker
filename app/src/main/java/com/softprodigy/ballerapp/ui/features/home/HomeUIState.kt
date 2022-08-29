package com.softprodigy.ballerapp.ui.features.home

import com.softprodigy.ballerapp.data.response.Team


data class HomeUIState(
    val isLoading: Boolean = false,
    val teams: ArrayList<Team> = ArrayList(),
    val showDialog: Boolean = false,
)

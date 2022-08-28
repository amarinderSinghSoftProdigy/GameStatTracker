package com.softprodigy.ballerapp.ui.features.user_type.team_setup

import com.softprodigy.ballerapp.data.response.Player

data class TeamSetupUIState(
    val teamColor: String = "",
    val teamName: String = "",
    val teamImageUri: String? = null,

    val isLoading: Boolean = false,
    val players: ArrayList<Player> = ArrayList(),
    val selectedPlayers: ArrayList<Player> = ArrayList(),
    val search: String = "",

    val showDialog: Boolean = false,
    val removePlayer: Player? = null,
)

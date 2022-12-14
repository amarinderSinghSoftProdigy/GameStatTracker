package com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated

import com.softprodigy.ballerapp.data.response.team.Player

data class TeamSetupUIStateUpdated(
    val teamColorPrimary: String = "",
    val teamColorSec: String = "",
    val teamColorThird: String = "",
    val teamName: String = "",
    val teamImageUri: String? = null,
    val teamImageServerUrl: String = "",

    val isLoading: Boolean = false,
    val players: ArrayList<Player> = ArrayList(),
    val selectedPlayers: ArrayList<Player> = ArrayList(),
    val search: String = "",

    val showDialog: Boolean = false,
    val removePlayer: Player? = null,

    /* Initialising default value with three*/
    val inviteMemberName: ArrayList<String> = arrayListOf("","",""),
    val inviteMemberEmail: ArrayList<String> = arrayListOf("","",""),
    var inviteMemberCount: Int = 3,
    )

package com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.softprodigy.ballerapp.data.response.Player

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

    val inviteMemberName: MutableList<String> = mutableListOf(),
    val inviteMemberEmail: MutableList<String> = mutableListOf(),
    var inviteMemberCount: Int = 0,

    )
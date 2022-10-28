package com.allballapp.android.ui.features.home.teams.manage_team

import com.google.gson.annotations.SerializedName
import com.allballapp.android.data.response.team.Player

data class ManageTeamRstrState(
    val isLoading: Boolean = false,
    val teamData: MutableList<ManageTeamData> = mutableListOf(),
    val showDialog: Boolean = false,
    val search: String = "",
    val selectedPlayers: MutableList<Player> = mutableListOf(),
    val matchedPlayers: MutableList<Player> = mutableListOf(),
    val allPlayer: MutableList<Player> = mutableListOf()
)

data class ManageTeamData(
    @SerializedName("id") val id: String = "",
    @SerializedName("name") val position: String = "",
    @SerializedName("data") val players: MutableList<TeamUser> = mutableListOf(),
)

data class TeamUser(
    @SerializedName("_id") val _id: String = "",
    @SerializedName("profileImage") val profileImage: String = "",
    @SerializedName("role") val role: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("points") val points: String = "",
)

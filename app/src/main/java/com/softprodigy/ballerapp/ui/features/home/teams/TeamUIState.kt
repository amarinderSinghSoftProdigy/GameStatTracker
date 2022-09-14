package com.softprodigy.ballerapp.ui.features.home.teams

import com.softprodigy.ballerapp.data.response.team.Coach
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.data.response.team.Team
import com.softprodigy.ballerapp.data.response.team.TeamLeaderBoard
import com.softprodigy.ballerapp.data.response.team.TeamRoaster


data class TeamUIState(
    val isLoading: Boolean = false,
    val teams: ArrayList<Team> = ArrayList(),
    val players: ArrayList<Player> = ArrayList(),
    val coaches: ArrayList<Coach> = ArrayList(),
    val leaderBoard: List<TeamLeaderBoard> = emptyList(),
    val roasterTabs: ArrayList<Player> = arrayListOf(),
    val roaster: List<TeamRoaster> = emptyList(),
    val selectedTeam: Team? = null,
    val showDialog: Boolean = false,
    val teamColor: String = "",
    val teamName: String = "",
    val teamImageUri: String? = null,
    val search: String = "",
    val selected: ArrayList<String> = ArrayList(),
    val checked: Boolean = false,
)

package com.softprodigy.ballerapp.ui.features.home.teams

import com.softprodigy.ballerapp.data.response.team.Coach
import com.softprodigy.ballerapp.data.response.team.LeaderboardPoint
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.data.response.team.Team


data class TeamUIState(
    val isLoading: Boolean = false,
    val teams: ArrayList<Team> = ArrayList(),
    val players: ArrayList<Player> = ArrayList(),
    val coaches: ArrayList<Coach> = ArrayList(),
    val leaderBoard: ArrayList<LeaderboardPoint> = ArrayList(),
    val selectedTeam: Team? = null,
    val showDialog: Boolean = false
)

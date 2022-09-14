package com.softprodigy.ballerapp.ui.features.home.teams.leaderboard

import com.softprodigy.ballerapp.data.response.LeaderBoard
import com.softprodigy.ballerapp.data.response.Standing
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.data.response.team.TeamLeaderBoard

data class LeaderBoardUiState(
    val isLoading: Boolean = false,
    val leaders: MutableList<LeaderBoard> = mutableListOf(),
    val selectedLeader:LeaderBoard=LeaderBoard(),
    val leaderBoard: List<TeamLeaderBoard> = emptyList(),
    val players: List<Player> = emptyList(),
    )
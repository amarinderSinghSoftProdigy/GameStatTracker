package com.allballapp.android.ui.features.home.teams.leaderboard

import com.allballapp.android.data.response.team.Player
import com.allballapp.android.data.response.team.TeamLeaderBoard

data class LeaderBoardUiState(
    val isLoading: Boolean = false,
    val leaders:  List<Player> = emptyList(),
    val selectedLeader:Player=Player(),
    val leaderBoard: List<TeamLeaderBoard> = emptyList(),
    val players: List<Player> = emptyList(),
    )
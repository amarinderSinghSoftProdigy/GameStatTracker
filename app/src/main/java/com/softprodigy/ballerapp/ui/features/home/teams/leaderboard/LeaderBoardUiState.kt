package com.softprodigy.ballerapp.ui.features.home.teams.leaderboard

import com.softprodigy.ballerapp.data.response.LeaderBoard
import com.softprodigy.ballerapp.data.response.Standing

data class LeaderBoardUiState(
    val isLoading: Boolean = false,
    val leaders: MutableList<LeaderBoard> = mutableListOf(),
    val selectedLeader:LeaderBoard=LeaderBoard()

)
package com.allballapp.android.ui.features.home.teams.leaderboard

import com.allballapp.android.data.response.team.Player

sealed class LeaderBoardUIEvent{
    data class OnLeaderSelected(val leader: Player) : LeaderBoardUIEvent()
    object RefreshLeaderBoardDat : LeaderBoardUIEvent()
}

package com.softprodigy.ballerapp.ui.features.home.teams.leaderboard

import com.softprodigy.ballerapp.data.response.LeaderBoard
import com.softprodigy.ballerapp.data.response.Standing
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.ui.features.home.teams.standing.StandingUIEvent

sealed class LeaderBoardUIEvent{
    data class OnLeaderSelected(val leader: Player) : LeaderBoardUIEvent()
    object RefreshLeaderBoardDat : LeaderBoardUIEvent()
}

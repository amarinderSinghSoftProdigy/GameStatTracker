package com.softprodigy.ballerapp.ui.features.home.manage_team.leaderboard

import com.softprodigy.ballerapp.data.response.ManageLeaderBoardResponse

data class ManageLeaderBoardUIState(
    val isLoading: Boolean = false,
    val selected: ArrayList<String> = ArrayList(),
    val checked: Boolean = false,
    var leaderBoardList: List<ManageLeaderBoardResponse> = emptyList()
)
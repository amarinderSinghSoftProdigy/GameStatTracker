package com.softprodigy.ballerapp.ui.features.home.manage_team.leaderboard


sealed class ManageLeaderBoardUIEvent {

    data class OnItemSelected(val name: String) : ManageLeaderBoardUIEvent()

}
package com.allballapp.android.ui.features.home.teams.manage_team

import com.allballapp.android.data.response.team.Player

sealed class ManageTeamRstrUIEvent {
    data class OnDialogClick(val showDialog:Boolean) : ManageTeamRstrUIEvent()
    data class OnSearch(val searchKey:String) : ManageTeamRstrUIEvent()
    data class OnPlayerClick(val player: Player):ManageTeamRstrUIEvent()
}
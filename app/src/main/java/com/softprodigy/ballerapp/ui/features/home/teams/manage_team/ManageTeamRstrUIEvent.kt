package com.softprodigy.ballerapp.ui.features.home.teams.manage_team

import com.softprodigy.ballerapp.data.response.Player

sealed class ManageTeamRstrUIEvent {
    data class OnDialogClick(val showDialog:Boolean) : ManageTeamRstrUIEvent()
    data class OnSearch(val searchKey:String) : ManageTeamRstrUIEvent()
    data class OnPlayerClick(val player:Player):ManageTeamRstrUIEvent()
}
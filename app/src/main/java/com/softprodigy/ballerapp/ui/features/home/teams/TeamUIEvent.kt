package com.softprodigy.ballerapp.ui.features.home.teams

import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.data.response.team.Team


sealed class TeamUIEvent {
    data class OnTeamSelected(val team: Team) : TeamUIEvent()
    data class OnConfirmTeamClick(val teamId: String,val teamName:String) : TeamUIEvent()
    object OnDismissClick : TeamUIEvent()
    data class ShowToast(val message: String) : TeamUIEvent()
    data class OnColorSelected(val selectedColor: String) : TeamUIEvent()
    data class OnTeamNameChange(val teamName: String) : TeamUIEvent()
    data class OnImageSelected(val teamImageUri: String) : TeamUIEvent()
    data class OnDialogClick(val showDialog: Boolean) : TeamUIEvent()
    data class OnSearch(val searchKey: String) : TeamUIEvent()
    data class OnPlayerClick(val player: Player) : TeamUIEvent()
    data class OnItemSelected(val name: String) : TeamUIEvent()
    object OnTeamUpdate : TeamUIEvent()
    data class OnTeamIdSelected(val teamId: String) : TeamUIEvent()
}
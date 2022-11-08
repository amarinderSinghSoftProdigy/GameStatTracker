package com.allballapp.android.ui.features.home.teams

import com.allballapp.android.data.request.Address
import com.allballapp.android.data.response.team.Player
import com.allballapp.android.data.response.team.Team


sealed class TeamUIEvent {
    data class OnTeamSelected(val team: Team) : TeamUIEvent()
    data class OnConfirmTeamClick(val teamId: String, val teamName: String) : TeamUIEvent()
    object OnDismissClick : TeamUIEvent()
    data class ShowToast(val message: String) : TeamUIEvent()
    data class OnColorSelected(val selectedColor: String) : TeamUIEvent()

    data class OnTeamNameChange(val teamName: String) : TeamUIEvent()
    data class OnImageSelected(val teamImageUri: String) : TeamUIEvent()
    data class OnDialogClick(val showDialog: Boolean) : TeamUIEvent()
    data class GetTeam(val teamId: String) : TeamUIEvent()
    data class OnSearch(val searchKey: String) : TeamUIEvent()
    data class OnPlayerClick(val player: Player) : TeamUIEvent()
    data class OnItemSelected(val name: String) : TeamUIEvent()
    object OnTeamUpdate : TeamUIEvent()
    data class OnTeamIdSelected(val teamId: String) : TeamUIEvent()

    data class OnSecColorSelected(val secondaryColor: String) : TeamUIEvent()
    data class OnTerColorSelected(val ternaryColor: String) : TeamUIEvent()
    data class OnTeamNameJerseyChange(val teamNameOnJersey: String) : TeamUIEvent()
    data class OnTeamNameTournamentsChange(val teamNameOnTournaments: String) : TeamUIEvent()
    data class OnVenueChange(val venueName: String) : TeamUIEvent()
    data class OnAddressChanged(val addressReq: Address) : TeamUIEvent()

}
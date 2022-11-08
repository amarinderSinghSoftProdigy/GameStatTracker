package com.allballapp.android.ui.features.user_type.team_setup.updated

import com.allballapp.android.data.request.Address
import com.allballapp.android.data.request.Members
import com.allballapp.android.data.response.UserRoles
import com.allballapp.android.data.response.team.Player


sealed class TeamSetupUIEventUpdated {
    data class OnColorSelected(val primaryColor: String) : TeamSetupUIEventUpdated()
    data class OnSecColorSelected(val secondaryColor: String) : TeamSetupUIEventUpdated()
    data class OnTerColorSelected(val ternaryColor: String) : TeamSetupUIEventUpdated()
    data class OnTeamNameChange(val teamName: String) : TeamSetupUIEventUpdated()
    data class OnImageSelected(val teamImageUri: String) : TeamSetupUIEventUpdated()
    object Clear : TeamSetupUIEventUpdated()
    object OnTeamSetupNextClick : TeamSetupUIEventUpdated()

    data class OnSearchPlayer(val searchPlayerQuery: String) : TeamSetupUIEventUpdated()
    data class OnAddPlayerClick(val player: Player) : TeamSetupUIEventUpdated()
    data class OnRemovePlayerClick(val player: Player) : TeamSetupUIEventUpdated()
    data class OnRemovePlayerConfirmClick(val player: Player) : TeamSetupUIEventUpdated()
    data class OnDismissDialogCLick(val showDialog: Boolean) : TeamSetupUIEventUpdated()
    object OnAddPlayerScreenNext : TeamSetupUIEventUpdated()
    data class OnRole(val role: String) : TeamSetupUIEventUpdated()
    data class SetRequestData(val role: String,val teamId: String) : TeamSetupUIEventUpdated()
    object OnLogoUploadSuccess : TeamSetupUIEventUpdated()
    object GetRoles : TeamSetupUIEventUpdated()
    data class MoveBack(val check: Boolean = false) : TeamSetupUIEventUpdated()

    data class OnContactAdded(val data: InviteObject) : TeamSetupUIEventUpdated()
    data class OnIndexChange(val index: Int) : TeamSetupUIEventUpdated()
    data class OnNameValueChange(val index: Int, val name: String) : TeamSetupUIEventUpdated()
    data class OnEmailValueChange(val index: Int, val email: String) : TeamSetupUIEventUpdated()
    data class OnCountryValueChange(val index: Int, val code: String) : TeamSetupUIEventUpdated()
    data class OnRoleValueChange(val index: Int, val role: UserRoles) : TeamSetupUIEventUpdated()
    data class OnInviteCountValueChange(val index: Int? = null, val addIntent: Boolean) :
        TeamSetupUIEventUpdated()

    data class OnInviteTeamMembers(
        val teamId: String,
        val userType: String = "",
        val type: String = "",
        val member: Members? = null,
        val profilesSelected: Boolean = false,
    ) : TeamSetupUIEventUpdated()

    object OnBackButtonClickFromPlayerScreen : TeamSetupUIEventUpdated()

    data class OnTeamNameJerseyChange(val teamNameOnJersey: String) : TeamSetupUIEventUpdated()
    data class OnTeamNameTournamentsChange(val teamNameOnTournaments: String) :
        TeamSetupUIEventUpdated()

    data class OnVenueChange(val venueName: String) : TeamSetupUIEventUpdated()
    data class OnAddressChange(val address: String) : TeamSetupUIEventUpdated()

    data class OnCoachNameChange(val coachName: String) : TeamSetupUIEventUpdated()
    data class OnCoachRoleChange(val coachRole: String) : TeamSetupUIEventUpdated()
    data class OnCoachEmailChange(val coachEmail: String) : TeamSetupUIEventUpdated()
    data class OnAddressChanged(val addressReq: Address) : TeamSetupUIEventUpdated()
    data class GetInvitedTeamPlayers(val teamId: String) : TeamSetupUIEventUpdated()
}
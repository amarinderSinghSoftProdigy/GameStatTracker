package com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated

import com.softprodigy.ballerapp.data.response.team.Player


sealed class TeamSetupUIEventUpdated {
    data class OnColorSelected(val primaryColor: String) : TeamSetupUIEventUpdated()
    data class OnSecColorSelected(val secondaryColor: String) : TeamSetupUIEventUpdated()
    data class OnTerColorSelected(val ternaryColor: String) : TeamSetupUIEventUpdated()
    data class OnTeamNameChange(val teamName: String) : TeamSetupUIEventUpdated()
    data class OnImageSelected(val teamImageUri: String) : TeamSetupUIEventUpdated()
    object OnTeamSetupNextClick : TeamSetupUIEventUpdated()

    data class OnSearchPlayer(val searchPlayerQuery: String) : TeamSetupUIEventUpdated()
    data class OnAddPlayerClick(val player: Player) : TeamSetupUIEventUpdated()
    data class OnRemovePlayerClick(val player: Player) : TeamSetupUIEventUpdated()
    data class OnRemovePlayerConfirmClick(val player: Player) : TeamSetupUIEventUpdated()
    data class OnDismissDialogCLick(val showDialog: Boolean) : TeamSetupUIEventUpdated()
    object OnAddPlayerScreenNext : TeamSetupUIEventUpdated()
    object OnLogoUploadSuccess : TeamSetupUIEventUpdated()

    data class OnNameValueChange(val index:Int,val name:String): TeamSetupUIEventUpdated()
    data class OnEmailValueChange(val index:Int,val email:String): TeamSetupUIEventUpdated()
    data class OnInviteCountValueChange(val index: Int? = null, val addIntent: Boolean): TeamSetupUIEventUpdated()

    data class OnInviteTeamMembers(val teamId:String): TeamSetupUIEventUpdated()
    object OnBackButtonClickFromPlayerScreen:TeamSetupUIEventUpdated()


}
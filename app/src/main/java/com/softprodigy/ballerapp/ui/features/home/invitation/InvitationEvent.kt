package com.softprodigy.ballerapp.ui.features.home.invitation

import com.softprodigy.ballerapp.data.response.PlayerDetails
import com.softprodigy.ballerapp.data.response.invitation.UserRoleModel

sealed class InvitationEvent {
    data class OnAcceptCLick(val invitation: Invitation) : InvitationEvent()
    data class OnDeclineCLick(val invitation: Invitation) : InvitationEvent()
    data class OnRoleClick(val roleKey: String) : InvitationEvent()
    data class OnRoleDialogClick(val showRoleDialog: Boolean) : InvitationEvent()
    data class OnDeleteDialogClick(val showDeleteDialog: Boolean) : InvitationEvent()
    data class OnPlayerAddedSuccessDialog(val showPlayerAddedDialog: Boolean) : InvitationEvent()
    object OnRoleConfirmClick : InvitationEvent()
    data class OnDeclineConfirmClick(val invitation: Invitation) : InvitationEvent()
    data class OnGuardianClick(val guardian: String) : InvitationEvent()
    data class OnGuardianDialogClick(val showGuardianDialog: Boolean) : InvitationEvent()
    data class OnAddPlayerDialogClick(val showAddPlayerDialog: Boolean) : InvitationEvent()
    object OnClearValues : InvitationEvent()
    object OnClearGuardianValues : InvitationEvent()
    data class OnValuesSelected(val playerDetails: PlayerDetails) : InvitationEvent()
    data class OnInvitationConfirm(val gender:String?) : InvitationEvent()
}
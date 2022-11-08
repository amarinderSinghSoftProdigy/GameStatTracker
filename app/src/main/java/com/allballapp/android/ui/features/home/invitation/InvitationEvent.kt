package com.allballapp.android.ui.features.home.invitation

import com.allballapp.android.data.response.PlayerDetails

sealed class InvitationEvent {
    data class SetTeamId(val teamId: String) : InvitationEvent()
    data class SetData(val logo: String, val playerName: String) : InvitationEvent()
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
    data class ConfirmGuardianWithoutChildAlert(val showConfirmDialog: Boolean) : InvitationEvent()
    object OnClearValues : InvitationEvent()
    object GetRoles : InvitationEvent()
    object OnClearGuardianValues : InvitationEvent()
    data class OnValuesSelected(val playerDetails: PlayerDetails) : InvitationEvent()
    data class OnInvitationConfirm(val gender: String?) : InvitationEvent()
}
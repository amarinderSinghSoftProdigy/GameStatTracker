package com.softprodigy.ballerapp.ui.features.home.invitation

sealed class InvitationEvent {
    data class OnAcceptCLick(val invitation: Invitation) : InvitationEvent()
    data class OnDeclineCLick(val invitation: Invitation) : InvitationEvent()
    data class OnRoleClick(val role: String) : InvitationEvent()
    data class OnRoleDialogClick(val showRoleDialog: Boolean) : InvitationEvent()
    data class OnDeleteDialogClick(val showDeleteDialog: Boolean) : InvitationEvent()
    object OnRoleConfirmClick : InvitationEvent()
    data class OnDeclineConfirmClick(val invitation: Invitation) : InvitationEvent()

}
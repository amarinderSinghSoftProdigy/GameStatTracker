package com.softprodigy.ballerapp.ui.features.home.invitation

sealed class InvitationEvent {
    data class OnAcceptCLick(val invitation: InvitationDemoModel) : InvitationEvent()
    data class OnDeclineCLick(val invitation: InvitationDemoModel) : InvitationEvent()
    data class OnRoleClick(val role: String) : InvitationEvent()
    data class OnRoleDialogClick(val showRoleDialog: Boolean) : InvitationEvent()
    data class OnDeleteDialogClick(val showDeleteDialog: Boolean) : InvitationEvent()
    data class OnDeclineConfirmClick(val invitation: InvitationDemoModel) : InvitationEvent()

}
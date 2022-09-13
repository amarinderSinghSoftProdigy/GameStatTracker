package com.softprodigy.ballerapp.ui.features.home.invitation

sealed class InvitationEvent {
    data class OnAcceptCLick(val invitation: InvitationDemoModel) : InvitationEvent()
    data class OnDeclineCLick(val invitation: InvitationDemoModel) : InvitationEvent()
    data class OnRoleClick(val role: String) : InvitationEvent()
    data class OnDialogClick(val showDialog: Boolean) : InvitationEvent()

}
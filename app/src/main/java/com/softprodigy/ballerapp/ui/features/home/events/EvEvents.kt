package com.softprodigy.ballerapp.ui.features.home.events

sealed class EvEvents {
    data class OnGoingCLick(val event: Events) : EvEvents()
    data class OnDeclineCLick(val event: Events) : EvEvents()
    data class OnRoleClick(val role: String) : EvEvents()
    data class OnRoleDialogClick(val showRoleDialog: Boolean) : EvEvents()
    data class onCancel(val showGoingDialog: Boolean) : EvEvents()
    data class onCancelDeclineDialog(val showDeclineDialog: Boolean) : EvEvents()
    object OnRoleConfirmClick : EvEvents()
    data class OnDeclineConfirmClick(val invitation: EventsResponse) : EvEvents()
    object RefreshEventScreen : EvEvents()
    object OnConfirmGoing : EvEvents()
    data class OnDeclineReasonChange(val reason: String) : EvEvents()
    data class OnGoingDialogClick(val showGoingDialog: Boolean) : EvEvents()

    data class OnSelection(val selected: String) : EvEvents()
    data class OnReasonSelection(val text: String) : EvEvents()
}
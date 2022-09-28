package com.softprodigy.ballerapp.ui.features.home.events

sealed class EvEvents {
    data class OnGoingCLick(val event: Events) : EvEvents()
    data class OnDeclineCLick(val event: Events) : EvEvents()
    data class OnRoleClick(val role: String) : EvEvents()
    data class OnRoleDialogClick(val showRoleDialog: Boolean) : EvEvents()
    data class onCancel(val showGoingDialog: Boolean) : EvEvents()
    data class onCancelDeclineDialog(val showDeclineDialog: Boolean) : EvEvents()
    object OnRoleConfirmClick : EvEvents()
    data class OnDeclineConfirmClick(val invitation: Events) : EvEvents()

    //Opportunities
    object GetFilters : EvEvents()
    object GetOpportunities : EvEvents()
    object GetOpportunityDetail : EvEvents()
    data class SetEventId(val id: String) : EvEvents()

}
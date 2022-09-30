package com.softprodigy.ballerapp.ui.features.home.events

import com.softprodigy.ballerapp.data.response.team.Team

sealed class EvEvents {
    data class OnGoingCLick(val event: Events) : EvEvents()
    data class OnDeclineCLick(val event: Events) : EvEvents()
    data class OnRoleClick(val role: String) : EvEvents()
    data class OnRoleDialogClick(val showRoleDialog: Boolean) : EvEvents()
    data class onCancel(val showGoingDialog: Boolean) : EvEvents()
    data class onCancelDeclineDialog(val showDeclineDialog: Boolean) : EvEvents()
    object OnRoleConfirmClick : EvEvents()

    //Opportunities
    object GetFilters : EvEvents()
    object GetOpportunities : EvEvents()
    object GetOpportunityDetail : EvEvents()
    data class SetEventId(val id: String) : EvEvents()
    data class GetDivisions(val id: String) : EvEvents()
    object RegisterForEvent : EvEvents()
    data class UpdateFilters(val request: FilterUpdateRequest) : EvEvents()

    data class RegisterTeam(val request: Team) : EvEvents()
    data class RegisterDivision(val request: DivisionData) : EvEvents()
    data class RegisterPlayer(val request: ArrayList<String>) : EvEvents()
    data class RegisterTerms(val request: Boolean) : EvEvents()
    data class RegisterNotification(val request: Boolean) : EvEvents()
    data class RegisterPrivacy(val request: Boolean) : EvEvents()
    data class RegisterCash(val request: String) : EvEvents()

    data class ShowToast(val message: String) : EvEvents()
    data class OnDeclineConfirmClick(val invitation: EventsResponse) : EvEvents()
    object RefreshEventScreen : EvEvents()
    object OnConfirmGoing : EvEvents()
    data class OnDeclineReasonChange(val reason: String) : EvEvents()
    data class OnGoingDialogClick(val showGoingDialog: Boolean) : EvEvents()

    data class OnSelection(val selected: String) : EvEvents()
    data class OnReasonSelection(val text: String) : EvEvents()
    object OnConfirmDeclineClick : EvEvents()

    object GetMyLeagues : EvEvents()

}
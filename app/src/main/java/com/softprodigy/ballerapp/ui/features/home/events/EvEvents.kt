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
    object ClearRegister : EvEvents()

    data class ShowToast(val message: String) : EvEvents()
    data class OnDeclineConfirmClick(val invitation: EventsResponse) : EvEvents()
    object RefreshEventScreen : EvEvents()
    data class RefreshEventDetailsScreen(val eventId: String) : EvEvents()
    object OnConfirmGoing : EvEvents()
    data class OnDeclineReasonChange(val reason: String) : EvEvents()
    data class OnGoingDialogClick(val showGoingDialog: Boolean) : EvEvents()
    data class OnSelection(val selected: String) : EvEvents()
    data class OnReasonSelection(val text: String) : EvEvents()
    object OnConfirmDeclineClick : EvEvents()
    data class PreNoteTimeSpan(val showPreNoteButton: Boolean) : EvEvents()
    data class PostNoteTimeSpan(val showPostNoteButton: Boolean) : EvEvents()
    data class ShowPrePostPracticeAddNoteDialog(
        val showPostNoteDialog: Boolean,
        val noteType: NoteType
    ) : EvEvents()

    data class OnNoteChange(val note: String) : EvEvents()
    data class OnAddNoteConfirmClick(
        val noteType: NoteType,
        val note: String,
        val eventId: String
    ) : EvEvents()

    object GetMyLeagues : EvEvents()

    object GetDivision : EvEvents()
    data class GetLeagueId(val id: String) : EvEvents()
    data class GetGender(val gender: String) : EvEvents()

    object GetVenues : EvEvents()
}

enum class NoteType(val type: String) {
    PRE("pre"),
    POST("post")
}

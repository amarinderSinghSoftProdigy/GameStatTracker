package com.allballapp.android.ui.features.home.events

import com.allballapp.android.data.response.Standing
import com.allballapp.android.data.response.team.Team

sealed class EvEvents {
    data class OnGoingCLick(val event: Events) : EvEvents()
    data class OnDeclineCLick(val event: Events) : EvEvents()
    data class OnRoleClick(val role: String) : EvEvents()
    data class OnRoleDialogClick(val showRoleDialog: Boolean) : EvEvents()
    data class onCancel(val showGoingDialog: Boolean) : EvEvents()
    data class onCancelDeclineDialog(val showDeclineDialog: Boolean) : EvEvents()
    object OnRoleConfirmClick : EvEvents()
    object GetRoles : EvEvents()

    //Opportunities
    object GetFilters : EvEvents()
    object GetRefereeFilters : EvEvents()
    data class GetOpportunities(val type: String = "") : EvEvents()
    object GetOpportunityDetail : EvEvents()
    data class SetEventId(val id: String) : EvEvents()
    data class GetDivisions(val id: String) : EvEvents()
    object RegisterForEvent : EvEvents()
    object RegisterGameStaff : EvEvents()
    data class UpdateFilters(val request: FilterUpdateRequest) : EvEvents()

    data class RegisterTeam(val request: Team) : EvEvents()
    data class RegisterDivision(val request: DivisionData) : EvEvents()
    data class RegisterPlayer(val request: ArrayList<String>) : EvEvents()
    data class RegisterTerms(val request: Boolean) : EvEvents()
    data class RegisterNotification(val request: Boolean) : EvEvents()
    data class RegisterPrivacy(val request: Boolean) : EvEvents()
    data class RegisterCash(val request: String) : EvEvents()
    object ClearRegister : EvEvents()
    object ClearList : EvEvents()
    object ClearListLeague : EvEvents()
    object ClearListEvents : EvEvents()
    object ClearOpportunities : EvEvents()
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

    data class GetMyLeagues(val type:String) : EvEvents()

    object GetDivision : EvEvents()
    data class GetLeagueId(val eventId: String) : EvEvents()
    data class GetGender(val gender: String) : EvEvents()

    object GetVenues : EvEvents()
    data class RefreshTeamsByLeagueAndDivision(val divisionId: String) : EvEvents()
    object RefreshTeamsByDivision : EvEvents()
    data class RefreshVenueDetailsById(val venueId: String) : EvEvents()
    data class OnLeagueDivisionStandingSelected(val standing: Standing) : EvEvents()
    data class RefreshStandingByLeagueDivision(val divisionId: String) : EvEvents()
    data class GenderSelected(val gender: String) : EvEvents()
    data class EventType(val eventType: String) : EvEvents()
    data class Format(val format: String) : EvEvents()
    data class PaymentOption(val paymentOption: String) : EvEvents()
    data class GetSchedule(val eventId: String) : EvEvents()
    object ClearRequest : EvEvents()
    object ClearTeam : EvEvents()
    //data class Format(val format: String) : EvEvents()
    //data class Format(val format: String) : EvEvents()
}

enum class NoteType(val type: String) {
    PRE("pre"),
    POST("post")
}

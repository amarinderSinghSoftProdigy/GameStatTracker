package com.softprodigy.ballerapp.ui.features.home.events

import com.softprodigy.ballerapp.data.response.DivisionResponse
import com.softprodigy.ballerapp.data.response.MyLeagueResponse
import com.softprodigy.ballerapp.data.response.VenuesId
import com.softprodigy.ballerapp.data.response.team.DivisionWiseTeamResponse
import com.softprodigy.ballerapp.data.response.team.Team
import com.softprodigy.ballerapp.data.response.team.TeamsByLeagueDivisionResponse
import com.softprodigy.ballerapp.ui.features.home.teams.standing.StandingLeagueDivisionUIState
import com.softprodigy.ballerapp.ui.features.home.teams.standing.StandingUIState
import com.softprodigy.ballerapp.ui.features.venue.VenueDetails


data class EventState(
    val isLoading: Boolean = false,
    val currentEvents: ArrayList<Events> = arrayListOf(),
    val pastEvents: ArrayList<Events> = arrayListOf(),
    val leagues: ArrayList<Leagues> = arrayListOf(),
    val oppotuntities: ArrayList<Leagues> = arrayListOf(),
    val showGoingDialog: Boolean = false,
    val showDeclineDialog: Boolean = false,
    val showLoading: Boolean = false,
    val selectedEvent: Events = Events(),
    val selectionTeam: String = "",
    val reasonTeam: String = "",
    val selectedEventId: String = "",
    val filterPreference: List<FilterPreference> = mutableListOf(),
    val filters: HashMap<String, ArrayList<FilterPreference>> = HashMap(),
    val eventDivision: List<DivisionData> = mutableListOf(),
    val opportunitiesList: List<OpportunitiesItem> = mutableListOf(),
    val opportunitiesDetail: OpportunitiesDetail = OpportunitiesDetail(),
    val divisionData: DivisionData = DivisionData(),
    val team: Team = Team(),
    val registerRequest: RegisterRequest = RegisterRequest(),
    val declineReason: String = "",
    val myLeaguesList: ArrayList<MyLeagueResponse> = arrayListOf(),
    val divisions: ArrayList<DivisionResponse> = arrayListOf(),
    val leagueId: String = "",
    val gender: String = "",
    val venuesList: List<VenuesId> = listOf(),
    val event: EventDetails = EventDetails(),
    val isPrePracticeTimeSpan: Boolean = false,
    val isPostPracticeTimeSpan: Boolean = false,
    val showPrePostNoteDialog: Boolean = false,
    val note: String = "",
    val noteType: NoteType? = null,
    val teamsByLeagueDivision: List<TeamsByLeagueDivisionResponse> = listOf(),
    val divisionWiseTeamResponse: List<DivisionWiseTeamResponse> = listOf(),
    val venueDetails: VenueDetails = VenueDetails(),
    val standingUIState: StandingLeagueDivisionUIState=StandingLeagueDivisionUIState()
)
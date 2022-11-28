package com.allballapp.android.ui.features.home.events

import com.allballapp.android.data.response.*
import com.allballapp.android.data.response.game.GameDetails
import com.allballapp.android.data.response.team.DivisionWiseTeamResponse
import com.allballapp.android.data.response.team.Team
import com.allballapp.android.data.response.team.TeamsByLeagueDivisionResponse
import com.allballapp.android.ui.features.home.teams.standing.StandingLeagueDivisionUIState
import com.allballapp.android.ui.features.venue.VenueDetails


data class EventState(
    val isLoading: Boolean = false,
    val upcomingAndGameData: List<Any> = arrayListOf(),
    //val currentEvents: ArrayList<Events> = arrayListOf(),
    val pastEvents: ArrayList<Events> = arrayListOf(),
    val leagues: ArrayList<Leagues> = arrayListOf(),
    val oppotuntities: ArrayList<Leagues> = arrayListOf(),
    val showGoingDialog: Boolean = false,
    val showDeclineDialog: Boolean = false,
    val showLoading: Boolean = false,
    val selectedMyEventId: String = "",
    val selectedMyEventType: String = "",
    val selectionTeam: String = "",
    val reasonTeam: String = "",
    val selectedEventId: String = "",
    val filterPreference: List<FilterPreference> = mutableListOf(),
    val filters: HashMap<String, ArrayList<FilterPreference>> = HashMap(),
    val eventDivision: List<DivisionData> = mutableListOf(),
    val opportunitiesList: List<OpportunitiesItem> = mutableListOf(),
    val opportunitiesDetail: OpportunitiesDetail = OpportunitiesDetail(),
    val location: com.allballapp.android.data.request.Location = com.allballapp.android.data.request.Location(),
    val divisionData: DivisionData = DivisionData(),
    val team: Team = Team(),
    val registerRequest: RegisterRequest = RegisterRequest(),
    val registerGameStaff: GameStaffRegisterRequest = GameStaffRegisterRequest(),
    val declineReason: String = "",
    val myLeaguesList: ArrayList<MyLeagueResponse> = arrayListOf(),
    val divisions: MutableList<DivisionResponse> = arrayListOf(),
    //val leagueId: String = "",
    val eventId: String = "",
    val gender: String = "",
    val venuesList: List<VenuesId> = listOf(),
    val event: EventDetails = EventDetails(),
    val gameDetails: GameDetails = GameDetails(),
    val isPrePracticeTimeSpan: Boolean = false,
    val isPostPracticeTimeSpan: Boolean = false,
    val showPrePostNoteDialog: Boolean = false,
    val note: String = "",
    val noteType: NoteType? = null,
    val teamsByLeagueDivision: List<TeamsByLeagueDivisionResponse> = listOf(),
    val divisionWiseTeamResponse: List<DivisionWiseTeamResponse> = listOf(),
    val venueDetails: VenueDetails = VenueDetails(),
    val standingUIState: StandingLeagueDivisionUIState = StandingLeagueDivisionUIState(),


    val genderList: ArrayList<GenderList> = arrayListOf(),
    val eventType: ArrayList<com.allballapp.android.data.response.EventType> = arrayListOf(),
    val formatList: ArrayList<Format> = arrayListOf(),
    val selectedGender: String = "",
    val selectedEventType: String = "",
    val selectedFormat: String = "",
    val scheduleResponse: List<ScheduleResponse> = mutableListOf(),
    val price: String? = null,
    val roles: List<UserRoles> = mutableListOf(),
    val selectedRole:String = ""
)
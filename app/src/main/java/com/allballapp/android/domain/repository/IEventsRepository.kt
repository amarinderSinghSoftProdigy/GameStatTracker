package com.allballapp.android.domain.repository

import com.allballapp.android.common.ResultWrapper
import com.allballapp.android.data.request.CreateEventReq
import com.allballapp.android.data.response.StandingByLeagueAndDivisionData
import com.allballapp.android.data.response.team.DivisionWiseTeamResponse
import com.allballapp.android.data.response.team.TeamsByLeagueDivisionResponse
import com.allballapp.android.domain.BaseResponse
import com.allballapp.android.ui.features.home.events.*
import com.allballapp.android.ui.features.venue.VenueDetails
import javax.inject.Singleton

@Singleton
interface IEventsRepository {
    suspend fun createEvent(createEvent: CreateEventReq): ResultWrapper<BaseResponse<Any>>
    suspend fun getAllevents(
        teamId:String,
        page: Int = 1,
        limit: Int = 50,
        sort: String = ""
    ): ResultWrapper<BaseResponse<EventsResponse>>

    suspend fun acceptEventInvite(eventId: String): ResultWrapper<BaseResponse<Any>>
    suspend fun getEventDetails(eventId: String): ResultWrapper<BaseResponse<EventDetails>>
    suspend fun addPrePostNote(
        eventId: String, note: String, noteType: NoteType,
    ): ResultWrapper<BaseResponse<Any>>

    suspend fun rejectEventInvite(eventId: String, reason: String): ResultWrapper<BaseResponse<Any>>
    suspend fun getFilters(): ResultWrapper<BaseResponse<FilterResponse>>
    suspend fun getEventOpportunities(teamId:String): ResultWrapper<BaseResponse<List<OpportunitiesItem>>>
    suspend fun getEventOpportunityDetails(id: String): ResultWrapper<BaseResponse<OpportunitiesDetail>>
    suspend fun getEventDivisions(id: String): ResultWrapper<BaseResponse<List<DivisionData>>>
    suspend fun registerForEvent(request: RegisterRequest): ResultWrapper<BaseResponse<Any>>
    suspend fun updateFilters(request: FilterUpdateRequest): ResultWrapper<BaseResponse<Any>>

    suspend fun getTeamsByLeagueAndDivision(
        page: Int = 1,
        limit: Int = 50,
        sort: String = "",
        leagueId: String,
        divisionId: String
    ): ResultWrapper<BaseResponse<ArrayList<TeamsByLeagueDivisionResponse>>>

    suspend fun getTeamsByLeagueIdAllDivision(leagueId: String): ResultWrapper<BaseResponse<ArrayList<DivisionWiseTeamResponse>>>

    suspend fun getAllTeamsStandingByLeaguedAndDivision(
        page: Int = 1,
        limit: Int = 50,
        sort: String = "",
        leagueId: String,
        divisionId: String
    ): ResultWrapper<BaseResponse<StandingByLeagueAndDivisionData>>

    suspend fun getVenueDetailsById(venueId: String): ResultWrapper<BaseResponse<VenueDetails>>

    suspend fun getEventScheduleDetails(eventId: String): ResultWrapper<BaseResponse<List<ScheduleResponse>>>

    suspend fun registerGameStaff(gameStaffRegisterRequest: GameStaffRegisterRequest): ResultWrapper<BaseResponse<Any>>
}
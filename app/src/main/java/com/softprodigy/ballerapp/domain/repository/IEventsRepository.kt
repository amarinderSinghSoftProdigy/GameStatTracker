package com.softprodigy.ballerapp.domain.repository

import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.data.request.CreateEventReq
import com.softprodigy.ballerapp.domain.BaseResponse
import com.softprodigy.ballerapp.ui.features.home.events.*
import javax.inject.Singleton

@Singleton
interface IEventsRepository {
    suspend fun createEvent(createEvent: CreateEventReq): ResultWrapper<BaseResponse<Any>>
    suspend fun getAllevents(
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
    suspend fun getEventOpportunities(): ResultWrapper<BaseResponse<List<OpportunitiesItem>>>
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
    ): ResultWrapper<BaseResponse<Any>>

    suspend fun getTeamsByLeagueIdAllDivision(leagueId: String): ResultWrapper<BaseResponse<Any>>

    suspend fun getAllTeamsStandingByLeaguedAndDivision(
        page: Int = 1,
        limit: Int = 50,
        sort: String = "",
        leagueId: String,
        divisionId: String
    ): ResultWrapper<BaseResponse<Any>>

    suspend fun getVenueDetailsById(venueId:String): ResultWrapper<BaseResponse<Any>>
}
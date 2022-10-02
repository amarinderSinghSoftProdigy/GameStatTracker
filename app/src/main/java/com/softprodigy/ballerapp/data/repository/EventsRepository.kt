package com.softprodigy.ballerapp.data.repository

import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.common.safeApiCall
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.request.CreateEventReq
import com.softprodigy.ballerapp.domain.BaseResponse
import com.softprodigy.ballerapp.domain.repository.IEventsRepository
import com.softprodigy.ballerapp.network.APIService
import com.softprodigy.ballerapp.ui.features.home.events.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.FormBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventsRepository @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val service: APIService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IEventsRepository {
    override suspend fun createEvent(createEvent: CreateEventReq): ResultWrapper<BaseResponse<Any>> {
        return safeApiCall(dispatcher) {
            service.createNewEvent(createEvent)
        }
    }

    override suspend fun getAllevents(
        page: Int,
        limit: Int,
        sort: String
    ): ResultWrapper<BaseResponse<EventsResponse>> {
        return safeApiCall(dispatcher) {
            service.getAllevents(page, limit, sort)
        }
    }

    override suspend fun acceptEventInvite(eventId: String): ResultWrapper<BaseResponse<Any>> {
        val request: RequestBody = FormBody.Builder()
            .add("eventId", eventId).build()
        return safeApiCall(dispatcher) {
            service.acceptEventInvite(request)
        }
    }

    override suspend fun getEventDetails(eventId: String): ResultWrapper<BaseResponse<EventDetails>> {
        return safeApiCall(dispatcher) {
            service.getEventDetails(eventId)
        }
    }

    override suspend fun addPrePostNote(
        eventId: String,
        note: String,
        noteType: NoteType
    ): ResultWrapper<BaseResponse<Any>> {
        val request: RequestBody = if (noteType == NoteType.PRE) {
            FormBody.Builder().add("eventId", eventId)
                .add("prePractice", note)
                .build()
        } else {
            FormBody.Builder().add("eventId", eventId)
                .add("postPractice", note)
                .build()
        }
        return safeApiCall(dispatcher) {
            service.updateEventNote(request)
        }
    }

    override suspend fun rejectEventInvite(
        eventId: String,
        reason: String
    ): ResultWrapper<BaseResponse<Any>> {
        val request: RequestBody = FormBody.Builder()
            .add("eventId", eventId)
            .add("reason", reason)
            .build()
        return safeApiCall(dispatcher) {
            service.rejectEventInvite(request)
        }
    }

    override suspend fun getFilters(): ResultWrapper<BaseResponse<FilterResponse>> {
        return safeApiCall(dispatcher) {
            service.getFilters()
        }
    }

    override suspend fun getEventOpportunities(): ResultWrapper<BaseResponse<List<OpportunitiesItem>>> {
        return safeApiCall(dispatcher) {
            service.getAllOpportunities()
        }
    }

    override suspend fun getEventOpportunityDetails(id: String): ResultWrapper<BaseResponse<OpportunitiesDetail>> {
        return safeApiCall(dispatcher) {
            service.getOpportunityDetail(id)
        }
    }

    override suspend fun getEventDivisions(id: String): ResultWrapper<BaseResponse<List<DivisionData>>> {
        return safeApiCall(dispatcher) {
            service.getEventDivision(AppConstants.PAGE, AppConstants.PAGE_LIMIT, id)
        }
    }

    override suspend fun registerForEvent(request: RegisterRequest): ResultWrapper<BaseResponse<Any>> {
        return safeApiCall(dispatcher) {
            service.registerEvent(request)
        }
    }

    override suspend fun updateFilters(request: FilterUpdateRequest): ResultWrapper<BaseResponse<Any>> {
        return safeApiCall(dispatcher) {
            service.updateFilters(request)
        }
    }

    override suspend fun getTeamsByLeagueAndDivision(
        page: Int,
        limit: Int,
        sort: String,
        leagueId: String,
        divisionId: String
    ): ResultWrapper<BaseResponse<Any>> {
        return safeApiCall(dispatcher) {
            service.getTeamsByLeagueAndDivision(
                page = page,
                limit = limit,
                sort = sort,
                leagueId = leagueId,
                divisionId = divisionId
            )
        }
    }

    override suspend fun getTeamsByLeagueIdAllDivision(leagueId: String): ResultWrapper<BaseResponse<Any>> {
        return safeApiCall(dispatcher) {
            service.getTeamsByLeagueIdAllDivision(leagueId = leagueId)
        }
    }

    override suspend fun getAllTeamsStandingByLeaguedAndDivision(
        page: Int,
        limit: Int,
        sort: String,
        leagueId: String,
        divisionId: String
    ): ResultWrapper<BaseResponse<Any>> {
        return safeApiCall(dispatcher) {
            service.getAllTeamsStandingByLeaguedAndDivision(
                page = page,
                limit = limit,
                sort = sort,
                leagueId = leagueId,
                divisionId = divisionId
            )
        }
    }

    override suspend fun getVenueDetailsById(venueId: String): ResultWrapper<BaseResponse<Any>> {
        return safeApiCall(dispatcher) {
            service.getVenueDetailsById(venueId = venueId)
        }
    }



}
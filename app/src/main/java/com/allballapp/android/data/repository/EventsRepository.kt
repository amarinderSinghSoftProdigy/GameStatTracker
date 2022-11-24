package com.allballapp.android.data.repository

import com.allballapp.android.common.AppConstants
import com.allballapp.android.common.ResultWrapper
import com.allballapp.android.common.safeApiCall
import com.allballapp.android.data.datastore.DataStoreManager
import com.allballapp.android.data.request.CreateEventReq
import com.allballapp.android.data.response.StandingByLeagueAndDivisionData
import com.allballapp.android.data.response.game.GameDetails
import com.allballapp.android.data.response.team.DivisionWiseTeamResponse
import com.allballapp.android.data.response.team.TeamsByLeagueDivisionResponse
import com.allballapp.android.domain.BaseResponse
import com.allballapp.android.domain.repository.IEventsRepository
import com.allballapp.android.network.APIService
import com.allballapp.android.ui.features.home.events.*
import com.allballapp.android.ui.features.venue.VenueDetails
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
        teamId: String,
        page: Int,
        limit: Int,
        sort: String
    ): ResultWrapper<BaseResponse<EventsResponse>> {
        return safeApiCall(dispatcher) {
            service.getAllevents(teamId, page, limit, sort)
        }
    }

    override suspend fun acceptEventInvite(
        eventId: String,
        eventType: String
    ): ResultWrapper<BaseResponse<Any>> {
        val request: RequestBody = FormBody.Builder()
            .add("eventId", eventId)
            .add("gameType", eventType.toLowerCase())
            .build()
        return safeApiCall(dispatcher) {
            service.acceptEventInvite(request)
        }
    }

    override suspend fun getEventDetails(
        eventId: String,
        eventType: String
    ): ResultWrapper<BaseResponse<EventDetails>> {
        return safeApiCall(dispatcher) {
            service.getEventDetails(eventId, eventType.toLowerCase())
        }
    }

    override suspend fun getGameDetails(
        gameId: String,
        eventType: String
    ): ResultWrapper<BaseResponse<GameDetails>> {
        return safeApiCall(dispatcher) {
            service.getGameDetails(gameId, eventType.toLowerCase())
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
        reason: String,
        eventType: String,
    ): ResultWrapper<BaseResponse<Any>> {
        val request: RequestBody = FormBody.Builder()
            .add("eventId", eventId)
            .add("reason", reason)
            .add("gameType", eventType.toLowerCase())
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

    override suspend fun getEventOpportunities(
        type: String,
        teamId: String
    ): ResultWrapper<BaseResponse<List<OpportunitiesItem>>> {
        return safeApiCall(dispatcher) {
            service.getAllOpportunities(type, teamId)
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
    ): ResultWrapper<BaseResponse<ArrayList<TeamsByLeagueDivisionResponse>>> {
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

    override suspend fun getTeamsByLeagueIdAllDivision(leagueId: String): ResultWrapper<BaseResponse<ArrayList<DivisionWiseTeamResponse>>> {
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
    ): ResultWrapper<BaseResponse<StandingByLeagueAndDivisionData>> {
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

    override suspend fun getVenueDetailsById(
        venueId: String,
        eventId: String
    ): ResultWrapper<BaseResponse<VenueDetails>> {
        return safeApiCall(dispatcher) {
            service.getVenueDetailsById(venueId = venueId, eventId = eventId)
        }
    }


    override suspend fun getEventScheduleDetails(eventId: String): ResultWrapper<BaseResponse<List<ScheduleResponse>>> {
        return safeApiCall(dispatcher) {
            service.getEventSchedule(eventId = eventId)
        }
    }

    override suspend fun registerGameStaff(gameStaffRegisterRequest: GameStaffRegisterRequest): ResultWrapper<BaseResponse<Any>> {
        return safeApiCall(dispatcher) {
            service.registerGameStaff(gameStaffRegisterRequest)
        }
    }
}
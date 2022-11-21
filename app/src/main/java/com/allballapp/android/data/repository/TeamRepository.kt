package com.allballapp.android.data.repository

import com.allballapp.android.common.ResultWrapper
import com.allballapp.android.common.safeApiCall
import com.allballapp.android.data.datastore.DataStoreManager
import com.allballapp.android.data.request.*
import com.allballapp.android.data.response.*
import com.allballapp.android.data.response.homepage.HomePageCoachModel
import com.allballapp.android.data.response.invitation.InvitationData
import com.allballapp.android.data.response.roaster.RoasterResponse
import com.allballapp.android.data.response.team.Player
import com.allballapp.android.data.response.team.Team
import com.allballapp.android.domain.BaseResponse
import com.allballapp.android.domain.repository.ITeamRepository
import com.allballapp.android.network.APIService
import com.allballapp.android.ui.features.home.invitation.AcceptInvitation
import com.allballapp.android.ui.features.home.invitation.Invitation
import com.allballapp.android.ui.features.venue.VenueDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.FormBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeamRepository @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val service: APIService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ITeamRepository {

    override suspend fun getAllPlayers(
        page: Int,
        limit: Int,
        searchPlayer: String
    ): ResultWrapper<BaseResponse<ArrayList<Player>>> {
        return safeApiCall(dispatcher) { service.getAllPlayers(page, limit, searchPlayer) }
    }

    override suspend fun createTeamAPI(request: CreateTeamRequest): ResultWrapper<BaseResponse<TeamData>> {
        return safeApiCall(dispatcher) { service.createTeam(request) }
    }

    override suspend fun getTeams(
        coachId: String
    ): ResultWrapper<BaseResponse<ArrayList<Team>>> {
        return safeApiCall(dispatcher) { service.getTeams(coachId = coachId) }
    }

    override suspend fun getTeamsUserId(
        coachId: String
    ): ResultWrapper<BaseResponse<com.allballapp.android.data.response.team.Result>> {
        return safeApiCall(dispatcher) { service.getTeamsUser(coachId = coachId) }
    }

    override suspend fun getTeamsByTeamID(teamId: String): ResultWrapper<BaseResponse<Team>> {
        return safeApiCall(dispatcher) { service.getTeamsByTeamId(teamId) }
    }


    override suspend fun getLeaderBoard(teamId: String): ResultWrapper<BaseResponse<Team>> {
        return safeApiCall(dispatcher) { service.getLeaderBoard(teamId) }

    }

    override suspend fun getTeamCoachPlayerByID(id: String): ResultWrapper<BaseResponse<RoasterResponse>> {
        return safeApiCall(dispatcher) { service.getCoachPlayersByID(id = id) }
    }

    override suspend fun updateTeamDetails(id: UpdateTeamDetailRequest): ResultWrapper<BaseResponse<Team>> {
        return safeApiCall(dispatcher) { service.updateTeamDetails(id) }
    }

    override suspend fun inviteMembersByTeamId(updateTeamRequest: UpdateTeamRequest): ResultWrapper<BaseResponse<InvitationData>> {
        return safeApiCall(dispatcher) { service.sendInvitation(updateTeamRequest = updateTeamRequest) }
    }

    override suspend fun getInviteMembersByTeamId(teamId: String): ResultWrapper<BaseResponse<List<Members>>> {
        return safeApiCall(dispatcher) { service.getInviteMembersByTeamId(teamId = teamId) }
    }

    override suspend fun getTeamsStanding(
        page: Int,
        limit: Int
    ): ResultWrapper<BaseResponse<StandingData>> {
        return safeApiCall(dispatcher) { service.getTeamStandings(page = page, limit = limit) }
    }


    override suspend fun getAllInvitation(
        page: Int,
        limit: Int,
        sort: String
    ): ResultWrapper<BaseResponse<ArrayList<Invitation>>> {
        return safeApiCall(dispatcher) {
            service.getAllInvitation(page, limit, sort)
        }
    }

    override suspend fun acceptTeamInvitation(
        invitationId: String,
        role: String,
        playerId: String,//ArrayList<String>,
        guardianGender: String
    ): ResultWrapper<BaseResponse<AcceptInvitation>> {
        /*val request: RequestBody = FormBody.Builder()
            .add("invitationId", invitationId)
            .add("role", role)
            .add("kidId", playerId)
            .add("guardianGender", guardianGender)
            .build()*/
        val request = InviteMembersRequest(invitationId, role, playerId, guardianGender)
        return safeApiCall(dispatcher) {
            service.acceptTeamInvitation(request)
        }
    }

    override suspend fun rejectTeamInvitation(invitationId: String): ResultWrapper<BaseResponse<Any>> {
        val request: RequestBody = FormBody.Builder()
            .add("invitationId", invitationId)
            .build()
        return safeApiCall(dispatcher) {
            service.rejectTeamInvitation(request)
        }
    }

    override suspend fun getHomePageDetails(teamId: String): ResultWrapper<BaseResponse<HomePageCoachModel>> {
        return safeApiCall(dispatcher) {
            service.getHomePageDetails(teamId)
        }
    }

    override suspend fun getUserRoles(role: String): ResultWrapper<BaseResponse<List<UserRoles>>> {
        return safeApiCall(dispatcher) {
            service.getUserRoles(role)
        }
    }

    override suspend fun getPlayerById(
        id: String,
        eventRegistration: String
    ): ResultWrapper<BaseResponse<ArrayList<PlayerDetails>>> {
        return safeApiCall(dispatcher) {
            service.getTeamPlayerById(id, eventRegistration)
        }
    }

    override suspend fun getAllVenue(
        search: String,
        page: Int,
        limit: Int
    ): ResultWrapper<BaseResponse<ArrayList<VenueDetails>>> {
        return safeApiCall(dispatcher) {
            service.getAllVenue(search = search, page = page, limit = limit)
        }
    }

    override suspend fun getMyLeagues(
        type: String,
        teamId: String,
        page: Int,
        limit: Int,
        sort: String
    ): ResultWrapper<BaseResponse<ArrayList<MyLeagueResponse>>> {
        return safeApiCall(dispatcher) {
            service.getMyLeagues(
                type = type,
                teamId = teamId,
                page = page,
                limit = limit,
                sort = sort
            )
        }
    }

    override suspend fun getDivisions(
        page: Int,
        limit: Int,
        sort: String,
        gender: String,
        leagueId: String
    ): ResultWrapper<BaseResponse<ArrayList<DivisionResponse>>> {
        return safeApiCall(dispatcher) {
            service.getDivisions(
                page = page,
                limit = limit,
                sort = sort,
                leagueId = leagueId,
                gender = gender
            )
        }
    }

    override suspend fun getVenues(
        page: Int,
        limit: Int,
        sort: String,
        leagueId: String
    ): ResultWrapper<BaseResponse<VenuesResponse>> {
        return safeApiCall(dispatcher) {
            service.getVenues(
                page = page,
                limit = limit,
                sort = sort,
                leagueId = leagueId,
            )
        }
    }
}
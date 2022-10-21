package com.softprodigy.ballerapp.data.repository

import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.common.safeApiCall
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.request.CreateTeamRequest
import com.softprodigy.ballerapp.data.request.UpdateTeamDetailRequest
import com.softprodigy.ballerapp.data.request.UpdateTeamRequest
import com.softprodigy.ballerapp.data.response.*
import com.softprodigy.ballerapp.data.response.homepage.HomePageCoachModel
import com.softprodigy.ballerapp.data.response.roaster.RoasterResponse
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.data.response.team.Team
import com.softprodigy.ballerapp.data.response.team.TeamParent
import com.softprodigy.ballerapp.domain.BaseResponse
import com.softprodigy.ballerapp.domain.repository.ITeamRepository
import com.softprodigy.ballerapp.network.APIService
import com.softprodigy.ballerapp.ui.features.home.invitation.Invitation
import com.softprodigy.ballerapp.ui.features.venue.Venue
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

    override suspend fun createTeamAPI(request: CreateTeamRequest): ResultWrapper<BaseResponse<CreateTeamResponse>> {
        return safeApiCall(dispatcher) { service.createTeam(request) }
    }

    override suspend fun getTeams(
        coachId: String
    ): ResultWrapper<BaseResponse<ArrayList<Team>>> {
        return safeApiCall(dispatcher) { service.getTeams(coachId = coachId) }
    }

    override suspend fun getTeamsUserId(
        coachId: String
    ): ResultWrapper<BaseResponse<ArrayList<TeamParent>>> {
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

    override suspend fun inviteMembersByTeamId(updateTeamRequest: UpdateTeamRequest): ResultWrapper<BaseResponse<Any>> {
        return safeApiCall(dispatcher) { service.sendInvitation(updateTeamRequest = updateTeamRequest) }
    }

    override suspend fun getInviteMembersByTeamId(teamId: String): ResultWrapper<BaseResponse<Any>> {
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
        playerId: String,
        playerGender: String
    ): ResultWrapper<BaseResponse<Any>> {
        val request: RequestBody = FormBody.Builder()
            .add("invitationId", invitationId)
            .add("role", role)
            .add("kidId", playerId)
            .add("guardianGender", playerGender)
            .build()
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

    override suspend fun getHomePageDetails(): ResultWrapper<BaseResponse<HomePageCoachModel>> {
        return safeApiCall(dispatcher) {
            service.getHomePageDetails()
        }
    }

    override suspend fun getUserRoles(role: String): ResultWrapper<BaseResponse<ArrayList<String>>> {
        return safeApiCall(dispatcher) {
            service.getUserRoles(role)
        }
    }

    override suspend fun getPlayerById(id: String): ResultWrapper<BaseResponse<ArrayList<PlayerDetails>>> {
        return safeApiCall(dispatcher) {
            service.getTeamPlayerById(id)
        }
    }

    override suspend fun getAllVenue(
        page: Int,
        limit: Int
    ): ResultWrapper<BaseResponse<ArrayList<Venue>>> {
        return safeApiCall(dispatcher) {
            service.getAllVenue(page = page, limit = limit)
        }
    }

    override suspend fun getMyLeagues(
        page: Int,
        limit: Int,
        sort: String
    ): ResultWrapper<BaseResponse<ArrayList<MyLeagueResponse>>> {
        return safeApiCall(dispatcher) {
            service.getMyLeagues(page = page, limit = limit, sort = sort)
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
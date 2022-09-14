package com.softprodigy.ballerapp.data.repository

import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.common.safeApiCall
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.request.CreateTeamRequest
import com.softprodigy.ballerapp.data.request.UpdateTeamRequest
import com.softprodigy.ballerapp.data.response.CreateTeamResponse
import com.softprodigy.ballerapp.data.response.Standing
import com.softprodigy.ballerapp.data.response.roaster.RoasterResponse
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.data.response.team.Team
import com.softprodigy.ballerapp.domain.BaseResponse
import com.softprodigy.ballerapp.domain.repository.ITeamRepository
import com.softprodigy.ballerapp.network.APIService
import com.softprodigy.ballerapp.ui.features.home.invitation.Invitation
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
        page: Int,
        limit: Int,
        sort: String
    ): ResultWrapper<BaseResponse<ArrayList<Team>>> {
        return safeApiCall(dispatcher) { service.getTeams(page, limit, sort) }
    }

    override suspend fun getTeamsByTeamID(teamId: String): ResultWrapper<BaseResponse<Team>> {
        return safeApiCall(dispatcher) { service.getTeamsByTeamId(teamId) }

    }

    override suspend fun getTeamCoachPlayerByID(id: String): ResultWrapper<BaseResponse<RoasterResponse>> {
        return safeApiCall(dispatcher) { service.getCoachPlayersByID(id = id) }
    }

    override suspend fun inviteMembersByTeamId(updateTeamRequest: UpdateTeamRequest): ResultWrapper<BaseResponse<Any>> {
        return safeApiCall(dispatcher) { service.sendInvitation(updateTeamRequest = updateTeamRequest) }
    }

    override suspend fun getTeamsStanding(
        page: Int,
        limit: Int
    ): ResultWrapper<BaseResponse<ArrayList<Standing>>> {
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
        role: String
    ): ResultWrapper<BaseResponse<Any>> {
        val request: RequestBody = FormBody.Builder()
            .add("invitationId", invitationId)
            .add("role", role)
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

}
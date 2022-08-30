package com.softprodigy.ballerapp.data.repository

import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.common.safeApiCall
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.request.CreateTeamRequest
import com.softprodigy.ballerapp.data.response.CreateTeamResponse
import com.softprodigy.ballerapp.data.response.Player
import com.softprodigy.ballerapp.data.response.Team
import com.softprodigy.ballerapp.domain.BaseResponse
import com.softprodigy.ballerapp.domain.repository.ITeamRepository
import com.softprodigy.ballerapp.network.APIService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
}
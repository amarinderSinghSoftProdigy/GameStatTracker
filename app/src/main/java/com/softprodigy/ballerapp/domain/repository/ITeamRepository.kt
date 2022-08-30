package com.softprodigy.ballerapp.domain.repository

import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.data.request.CreateTeamRequest
import com.softprodigy.ballerapp.data.response.CreateTeamResponse
import com.softprodigy.ballerapp.data.response.Player
import com.softprodigy.ballerapp.data.response.Team
import com.softprodigy.ballerapp.domain.BaseResponse
import javax.inject.Singleton

@Singleton
interface ITeamRepository {
    suspend fun getAllPlayers(
        page: Int = 1,
        limit: Int = 10,
        searchPlayer: String
    ): ResultWrapper<BaseResponse<ArrayList<Player>>>

    suspend fun createTeamAPI(request: CreateTeamRequest): ResultWrapper<BaseResponse<CreateTeamResponse>>

    suspend fun getTeams(page: Int=1, limit: Int=5, sort: String=""): ResultWrapper<BaseResponse<ArrayList<Team>>>
}
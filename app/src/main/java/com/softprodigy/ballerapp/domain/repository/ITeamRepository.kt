package com.softprodigy.ballerapp.domain.repository

import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.data.response.Player
import com.softprodigy.ballerapp.domain.BaseResponse
import javax.inject.Singleton

@Singleton
interface ITeamRepository {
    suspend fun getAllPlayers(
        page: Int = 1,
        limit: Int = 10,
        searchPlayer: String
    ): ResultWrapper<BaseResponse<ArrayList<Player>>>
}
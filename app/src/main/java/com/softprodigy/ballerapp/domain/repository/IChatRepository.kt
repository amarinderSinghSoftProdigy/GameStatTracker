package com.softprodigy.ballerapp.domain.repository

import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.data.response.team.Team
import com.softprodigy.ballerapp.domain.BaseResponse
import javax.inject.Singleton

@Singleton
interface IChatRepository {
    suspend fun getAllChats(
        page: Int = 1,
        limit: Int = 10,
        sort: String = "",
        userId: String
    ): ResultWrapper<BaseResponse<ArrayList<Team>>>

}
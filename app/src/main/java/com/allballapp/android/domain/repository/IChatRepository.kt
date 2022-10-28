package com.allballapp.android.domain.repository

import com.allballapp.android.common.ResultWrapper
import com.allballapp.android.data.response.team.Team
import com.allballapp.android.domain.BaseResponse
import javax.inject.Singleton

@Singleton
interface IChatRepository {
    suspend fun getAllChats(
        page: Int = 1,
        limit: Int = 10,
        sort: String = "",
        userId: String
    ): ResultWrapper<BaseResponse<ArrayList<Team>>>

    suspend fun saveChatGroup(
        teamId: String,
        groupId: String
    ): ResultWrapper<BaseResponse<Any>>

}
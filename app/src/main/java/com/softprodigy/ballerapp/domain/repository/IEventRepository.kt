package com.softprodigy.ballerapp.domain.repository

import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.data.request.CreateTeamRequest
import com.softprodigy.ballerapp.domain.BaseResponse
import javax.inject.Singleton

@Singleton
interface IEventRepository {
    suspend fun createEvent(createTeamRequest: CreateTeamRequest): ResultWrapper<BaseResponse<Any>>
}
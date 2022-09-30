package com.softprodigy.ballerapp.domain.repository

import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.data.request.CreateEventReq
import com.softprodigy.ballerapp.data.request.CreateTeamRequest
import com.softprodigy.ballerapp.domain.BaseResponse
import com.softprodigy.ballerapp.ui.features.home.events.EventsResponse
import javax.inject.Singleton

@Singleton
interface IEventRepository {
    suspend fun createEvent(createEvent: CreateEventReq): ResultWrapper<BaseResponse<Any>>

    suspend fun getAllevents(
        page: Int = 1,
        limit: Int = 50,
        sort: String = ""
    ): ResultWrapper<BaseResponse<EventsResponse>>

    suspend fun acceptEventInvite(eventId:String):ResultWrapper<BaseResponse<Any>>

    suspend fun rejectEventInvite(eventId:String,reason:String):ResultWrapper<BaseResponse<Any>>

}
package com.softprodigy.ballerapp.data.repository

import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.common.safeApiCall
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.request.CreateEventReq
import com.softprodigy.ballerapp.domain.BaseResponse
import com.softprodigy.ballerapp.domain.repository.IEventRepository
import com.softprodigy.ballerapp.network.APIService
import com.softprodigy.ballerapp.ui.features.home.events.EventsResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.FormBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val service: APIService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IEventRepository {
    override suspend fun createEvent(createEvent: CreateEventReq): ResultWrapper<BaseResponse<Any>> {
        return safeApiCall(dispatcher) {
            service.createNewEvent(createEvent)
        }
    }

    override suspend fun getAllevents(
        page: Int,
        limit: Int,
        sort: String
    ): ResultWrapper<BaseResponse<EventsResponse>> {
        return safeApiCall(dispatcher) {
            service.getAllevents(page, limit, sort)
        }
    }

    override suspend fun acceptEventInvite(eventId: String): ResultWrapper<BaseResponse<Any>> {
        val request: RequestBody = FormBody.Builder()
            .add("eventId", eventId).build()
        return safeApiCall(dispatcher) {
            service.acceptEventInvite(request)
        }
    }
}
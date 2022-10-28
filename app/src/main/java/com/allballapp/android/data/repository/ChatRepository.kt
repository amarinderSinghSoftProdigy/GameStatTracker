package com.allballapp.android.data.repository

import com.allballapp.android.common.ResultWrapper
import com.allballapp.android.common.safeApiCall
import com.allballapp.android.data.datastore.DataStoreManager
import com.allballapp.android.data.response.team.Team
import com.allballapp.android.domain.BaseResponse
import com.allballapp.android.domain.repository.IChatRepository
import com.allballapp.android.network.APIService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.FormBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val service: APIService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IChatRepository {
    override suspend fun getAllChats(
        page: Int,
        limit: Int,
        sort: String,
        userId: String
    ): ResultWrapper<BaseResponse<ArrayList<Team>>> {
        return safeApiCall(dispatcher) { service.getAllChats(page, limit, sort, userId) }
    }

    override suspend fun saveChatGroup(
        teamId: String,
        groupId: String
    ): ResultWrapper<BaseResponse<Any>> {
        val request: RequestBody = FormBody.Builder()
            .add("teamId", teamId)
            .add("groupId", groupId)
            .build()
        return safeApiCall(dispatcher) { service.saveChatGroup(request) }
    }

}
package com.softprodigy.ballerapp.data.repository

import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.common.safeApiCall
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.request.LoginRequest
import com.softprodigy.ballerapp.data.response.UserInfo
import com.softprodigy.ballerapp.domain.BaseResponse
import com.softprodigy.ballerapp.domain.repository.IUserRepository
import com.softprodigy.ballerapp.network.APIService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val service: APIService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IUserRepository {
    override suspend fun userLogin(
        loginRequest: LoginRequest
    ): ResultWrapper<BaseResponse<UserInfo>> {
        return safeApiCall(dispatcher) { service.userLogin(loginRequest) }
    }
}
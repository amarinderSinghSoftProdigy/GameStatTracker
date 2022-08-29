package com.softprodigy.ballerapp.data.repository

import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.common.safeApiCall
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.request.ConfirmPhoneRequest
import com.softprodigy.ballerapp.data.request.LoginRequest
import com.softprodigy.ballerapp.data.request.VerifyPhoneRequest
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

    override suspend fun loginWithEmailAndPass(
        email: String,
        password: String,
    ): ResultWrapper<BaseResponse<UserInfo>> {
        return safeApiCall(dispatcher) { service.userLogin(loginRequest) }
    }

    override suspend fun verifyPhone(phone: String): ResultWrapper<BaseResponse<Any>> {
        val requestBody = VerifyPhoneRequest(phone = phone)
        return safeApiCall(dispatcher = dispatcher) { service.verifyPhone(requestBody) }
    }

    override suspend fun confirmPhone(
        phone: String,
        otp: String
    ): ResultWrapper<BaseResponse<Any>> {
        val requestBody = ConfirmPhoneRequest(phone = phone, otp = otp)
        return safeApiCall(dispatcher = dispatcher) { service.confirmPhone(requestBody) }
    }
}
package com.softprodigy.ballerapp.domain.repository

import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.data.request.LoginRequest
import com.softprodigy.ballerapp.data.response.UserInfo
import com.softprodigy.ballerapp.domain.BaseResponse
import javax.inject.Singleton


@Singleton
interface IUserRepository {
    suspend fun userLogin(
        loginRequest: LoginRequest
    ): ResultWrapper<BaseResponse<UserInfo>>
}
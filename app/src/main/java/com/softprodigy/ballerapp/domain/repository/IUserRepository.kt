package com.softprodigy.ballerapp.domain.repository

import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.data.UserInfo
import com.softprodigy.ballerapp.domain.BaseResponse
import javax.inject.Singleton


@Singleton
interface IUserRepository {
    suspend fun loginWithEmailAndPass(
        email: String,
        password: String
    ): ResultWrapper<BaseResponse<UserInfo>>

    suspend fun verifyPhone(phone: String): ResultWrapper<BaseResponse<Any>>

    suspend fun confirmPhone(phone: String, otp: String): ResultWrapper<BaseResponse<Any>>
}
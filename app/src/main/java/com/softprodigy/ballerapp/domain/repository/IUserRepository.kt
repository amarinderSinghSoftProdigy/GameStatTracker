package com.softprodigy.ballerapp.domain.repository

import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.data.request.LoginRequest
import com.softprodigy.ballerapp.data.request.SignUpData
import com.softprodigy.ballerapp.data.request.UpdateUserDetailsReq
import com.softprodigy.ballerapp.data.response.User
import com.softprodigy.ballerapp.data.response.UserInfo
import com.softprodigy.ballerapp.domain.BaseResponse
import javax.inject.Singleton


@Singleton
interface IUserRepository {
    suspend fun userLogin(
        loginRequest: LoginRequest
    ): ResultWrapper<BaseResponse<UserInfo>>

    suspend fun verifyPhone(phone: String): ResultWrapper<BaseResponse<Any>>

    suspend fun confirmPhone(phone: String, otp: String): ResultWrapper<BaseResponse<Any>>

    suspend fun signUp(signUpData: SignUpData): ResultWrapper<BaseResponse<UserInfo>>

    suspend fun forgotPassword(email: String): ResultWrapper<BaseResponse<Any>>

    suspend fun updateUserProfile(userProfile: SignUpData): ResultWrapper<BaseResponse<UserInfo>>

    suspend fun getUserProfile(): ResultWrapper<BaseResponse<User>>

    suspend fun getFullUserFullDetails(): ResultWrapper<BaseResponse<User>>

    suspend fun updateUserFullDetails(userDetailsReq: UpdateUserDetailsReq): ResultWrapper<BaseResponse<Any>>


}
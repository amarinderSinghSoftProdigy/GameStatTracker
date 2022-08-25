package com.softprodigy.ballerapp.network

import com.softprodigy.ballerapp.common.ApiConstants
import com.softprodigy.ballerapp.data.UserInfo
import com.softprodigy.ballerapp.data.request.LoginRequest
import com.softprodigy.ballerapp.domain.BaseResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface APIService {

    @POST(ApiConstants.LOGIN)
    suspend fun userLogin(@Body loginRequest: LoginRequest): BaseResponse<UserInfo>
}
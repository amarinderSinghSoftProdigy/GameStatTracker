package com.softprodigy.ballerapp.network

import com.softprodigy.ballerapp.common.ApiConstants
import com.softprodigy.ballerapp.data.request.LoginRequest
import com.softprodigy.ballerapp.data.response.Player
import com.softprodigy.ballerapp.data.response.UserInfo
import com.softprodigy.ballerapp.domain.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface APIService {

    @POST(ApiConstants.LOGIN)
    suspend fun userLogin(@Body loginRequest: LoginRequest): BaseResponse<UserInfo>

    @GET(ApiConstants.GET_ALL_PLAYERS)
    suspend fun getAllPlayers(
        @Query("page") page: Int,
        @Query("limit") order: Int,
        @Query("seachPlayer") seachPlayer: String
    ): BaseResponse<ArrayList<Player>>

}
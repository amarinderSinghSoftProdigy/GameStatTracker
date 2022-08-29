package com.softprodigy.ballerapp.network

import com.softprodigy.ballerapp.common.ApiConstants
import com.softprodigy.ballerapp.data.request.CreateTeamRequest
import com.softprodigy.ballerapp.data.request.LoginRequest
import com.softprodigy.ballerapp.data.response.CreateTeamResponse
import com.softprodigy.ballerapp.data.response.ImageUpload
import com.softprodigy.ballerapp.data.response.Player
import com.softprodigy.ballerapp.data.response.UserInfo
import com.softprodigy.ballerapp.domain.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface APIService {

    @POST(ApiConstants.LOGIN)
    suspend fun userLogin(@Body loginRequest: LoginRequest): BaseResponse<UserInfo>

    @GET(ApiConstants.GET_ALL_PLAYERS)
    suspend fun getAllPlayers(
        @Query("page") page: Int,
        @Query("limit") order: Int,
        @Query("seachPlayer") seachPlayer: String
    ): BaseResponse<ArrayList<Player>>

    @Multipart
    @POST(ApiConstants.UPLOAD_SINGLE_IMAGE)
    suspend fun uploadSingleImage(
        @Part("type") type: RequestBody,
        @Part file: MultipartBody.Part?
    ): BaseResponse<ImageUpload>

    @POST(ApiConstants.CREATE_TEAM)
    suspend fun createTeam(@Body request:CreateTeamRequest):BaseResponse<CreateTeamResponse>

}
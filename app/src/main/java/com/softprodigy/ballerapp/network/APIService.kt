package com.softprodigy.ballerapp.network

import com.softprodigy.ballerapp.common.ApiConstants
import com.softprodigy.ballerapp.data.request.ConfirmPhoneRequest
import com.softprodigy.ballerapp.data.request.CreateTeamRequest
import com.softprodigy.ballerapp.data.request.ForgotPasswordRequest
import com.softprodigy.ballerapp.data.request.LoginRequest
import com.softprodigy.ballerapp.data.request.SignUpData
import com.softprodigy.ballerapp.data.request.VerifyPhoneRequest
import com.softprodigy.ballerapp.data.response.CreateTeamResponse
import com.softprodigy.ballerapp.data.response.ImageUpload
import com.softprodigy.ballerapp.data.response.team.Team
import com.softprodigy.ballerapp.data.response.UserInfo
import com.softprodigy.ballerapp.data.response.roaster.RoasterResponse
import com.softprodigy.ballerapp.data.request.*
import com.softprodigy.ballerapp.data.response.*
import com.softprodigy.ballerapp.data.response.team.Player
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
    suspend fun createTeam(@Body request: CreateTeamRequest): BaseResponse<CreateTeamResponse>

    @GET(ApiConstants.GET_TEAMS)
    suspend fun getTeams(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("sort") sort: String
    ): BaseResponse<ArrayList<Team>>

    @GET("${ApiConstants.GET_TEAMS}/{id}")
    suspend fun getTeamsByTeamId(
        @Path("id") id: String
    ): BaseResponse<Team>

    @POST(ApiConstants.VERIFY_PHONE)
    suspend fun verifyPhone(@Body verifyPhoneRequest: VerifyPhoneRequest): BaseResponse<Any>

    @POST(ApiConstants.CONFIRM_PHONE)
    suspend fun confirmPhone(@Body confirmPhoneRequest: ConfirmPhoneRequest): BaseResponse<Any>

    @POST(ApiConstants.SIGNUP)
    suspend fun signUp(@Body signUpData: SignUpData): BaseResponse<UserInfo>

    @POST(ApiConstants.FORGOT_PASSWORD)
    suspend fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): BaseResponse<Any>

    @PUT(ApiConstants.UPDATE_PROFILE)
    suspend fun updateUserProfile(@Body userProfile: SignUpData): BaseResponse<UserInfo>

    @GET(ApiConstants.GET_TEAM_STANDING)
    suspend fun getTeamStandings(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): BaseResponse<ArrayList<Standing>>


    @GET("${ApiConstants.COACH_PLAYER}/{id}")
    suspend fun getCoachPlayersByID(@Path("id") id: String): BaseResponse<RoasterResponse>

    @PUT(ApiConstants.SEND_INVITATION)
    suspend fun sendInvitation(@Body updateTeamRequest:UpdateTeamRequest):BaseResponse<Any>

}
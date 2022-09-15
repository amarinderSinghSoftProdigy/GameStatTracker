package com.softprodigy.ballerapp.network

import com.softprodigy.ballerapp.common.ApiConstants
import com.softprodigy.ballerapp.data.request.ConfirmPhoneRequest
import com.softprodigy.ballerapp.data.request.CreateTeamRequest
import com.softprodigy.ballerapp.data.request.ForgotPasswordRequest
import com.softprodigy.ballerapp.data.request.LoginRequest
import com.softprodigy.ballerapp.data.request.SignUpData
import com.softprodigy.ballerapp.data.request.UpdateTeamDetailRequest
import com.softprodigy.ballerapp.data.request.UpdateTeamRequest
import com.softprodigy.ballerapp.data.request.VerifyPhoneRequest
import com.softprodigy.ballerapp.data.response.*
import com.softprodigy.ballerapp.data.response.homepage.HomePageCoachModel
import com.softprodigy.ballerapp.data.response.roaster.RoasterResponse
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.data.response.team.Team
import com.softprodigy.ballerapp.domain.BaseResponse
import com.softprodigy.ballerapp.ui.features.home.invitation.Invitation
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

open interface APIService {

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

    @GET(ApiConstants.GET_TEAM_BY_ID)
    suspend fun getTeamsByTeamId(
        @Query("teamId") id: String
    ): BaseResponse<Team>

    @GET(ApiConstants.GET_LEADER_BOARD)
    suspend fun getLeaderBoard(
        @Query("teamId") id: String
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
    ): BaseResponse<StandingData>


    @GET("${ApiConstants.COACH_PLAYER}/{id}")
    suspend fun getCoachPlayersByID(@Path("id") id: String): BaseResponse<RoasterResponse>

    @PUT(ApiConstants.SEND_INVITATION)
    suspend fun sendInvitation(@Body updateTeamRequest: UpdateTeamRequest): BaseResponse<Any>

    @GET(ApiConstants.GET_ALL_INVITATION)
    suspend fun getAllInvitation(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("sort") sort: String
    ): BaseResponse<ArrayList<Invitation>>

    @PUT(ApiConstants.ACCEPT_TEAM_INVITATION)
    suspend fun acceptTeamInvitation(@Body request: RequestBody): BaseResponse<Any>

    @PUT(ApiConstants.REJECT_TEAM_INVITATION)
    suspend fun rejectTeamInvitation(@Body request: RequestBody): BaseResponse<Any>

    @PUT(ApiConstants.UPDATE_TEAM)
    suspend fun updateTeamDetails(@Body updateTeamRequest: UpdateTeamDetailRequest): BaseResponse<Any>

    @GET(ApiConstants.GET_USER_DETAILS)
    suspend fun getUserDetails():BaseResponse<User>

    @GET(ApiConstants.GET_HOME_PAGE_DETAILS)
    suspend fun getHomePageDetails():BaseResponse<HomePageCoachModel>

}
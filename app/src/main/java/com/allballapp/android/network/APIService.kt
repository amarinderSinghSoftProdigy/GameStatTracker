package com.allballapp.android.network

import com.allballapp.android.common.ApiConstants
import com.allballapp.android.data.request.*
import com.allballapp.android.data.response.*
import com.allballapp.android.data.response.homepage.HomePageCoachModel
import com.allballapp.android.data.response.invitation.InvitationData
import com.allballapp.android.data.response.roaster.RoasterResponse
import com.allballapp.android.data.response.team.*
import com.allballapp.android.data.response.team.Team
import com.allballapp.android.domain.BaseResponse
import com.allballapp.android.ui.features.home.events.*
import com.allballapp.android.ui.features.home.invitation.Invitation
import com.allballapp.android.ui.features.venue.VenueDetails
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

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
        @Part("type") type: RequestBody, @Part file: MultipartBody.Part?
    ): BaseResponse<ImageUpload>

    @POST(ApiConstants.CREATE_TEAM)
    suspend fun createTeam(@Body request: CreateTeamRequest): BaseResponse<CreateTeamResponse>

    @GET(ApiConstants.GET_TEAMS_BY_COACH_ID)
    suspend fun getTeams(
        @Query("coachId") coachId: String,
    ): BaseResponse<ArrayList<Team>>

    @GET(ApiConstants.GET_TEAMS_BY_USER_ID)
    suspend fun getTeamsUser(
        @Query("userId") coachId: String,
    ): BaseResponse<Result>

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
    suspend fun confirmPhone(@Body confirmPhoneRequest: ConfirmPhoneRequest): BaseResponse<ProfileList>

    @POST(ApiConstants.SIGNUP)
    suspend fun signUp(@Body signUpData: SignUpData): BaseResponse<UserInfo>

    @POST(ApiConstants.FORGOT_PASSWORD)
    suspend fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): BaseResponse<Any>

    @PUT(ApiConstants.UPDATE_PROFILE)
    suspend fun updateUserProfile(@Body userProfile: SignUpPhoneData): BaseResponse<UserInfo>

    @GET(ApiConstants.GET_TEAM_STANDING)
    suspend fun getTeamStandings(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
    ): BaseResponse<StandingData>

    @GET("${ApiConstants.COACH_PLAYER}/{id}")
    suspend fun getCoachPlayersByID(@Path("id") id: String): BaseResponse<RoasterResponse>

    @PUT(ApiConstants.SEND_INVITATION)
    suspend fun sendInvitation(@Body updateTeamRequest: UpdateTeamRequest): BaseResponse<InvitationData>

    @GET(ApiConstants.GET_ALL_INVITATION)
    suspend fun getAllInvitation(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("sort") sort: String
    ): BaseResponse<ArrayList<Invitation>>

    @GET(ApiConstants.GET_ALL_EVENTS)
    suspend fun getAllevents(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("sort") sort: String
    ): BaseResponse<EventsResponse>

    @PUT(ApiConstants.ACCEPT_TEAM_INVITATION)
    suspend fun acceptTeamInvitation(@Body request: RequestBody): BaseResponse<Any>

    @PUT(ApiConstants.REJECT_TEAM_INVITATION)
    suspend fun rejectTeamInvitation(@Body request: RequestBody): BaseResponse<Any>

    @PUT(ApiConstants.UPDATE_TEAM)
    suspend fun updateTeamDetails(@Body updateTeamRequest: UpdateTeamDetailRequest): BaseResponse<Team>

    @GET(ApiConstants.GET_USER_DETAILS)
    suspend fun getUserDetails(): BaseResponse<User>

    @GET(ApiConstants.GET_HOME_PAGE_DETAILS)
    suspend fun getHomePageDetails(): BaseResponse<HomePageCoachModel>

    @GET(ApiConstants.GET_USER_ROLE)
    suspend fun getUserRoles(@Query("type") type: String): BaseResponse<List<UserRoles>>

    @GET(ApiConstants.GET_TEAM_PLAYER_BY_ID)
    suspend fun getTeamPlayerById(
        @Query("teamId") id: String,
        @Query("eventRegistration") eventRegistration: String
    ): BaseResponse<ArrayList<PlayerDetails>>

    @GET(ApiConstants.GET_USER_FULL_DETAILS)
    suspend fun getUserFullDetails(): BaseResponse<User>

    @PUT(ApiConstants.UPDATE_USER_FULL_DETAILS)
    suspend fun updateUserFullDetails(@Body userDetailsReq: UpdateUserDetailsReq): BaseResponse<Any>

    @PUT(ApiConstants.LEAVE_TEAM)
    suspend fun leaveTeam(@Body request: RequestBody): BaseResponse<Any>

    @GET(ApiConstants.DOC_TYPES)
    suspend fun getDocTypes(): BaseResponse<List<UserDocType>>

    @HTTP(method = "DELETE", path = ApiConstants.DELETE_DOC, hasBody = true)
    suspend fun deleteDoc(@Body request: RequestBody): BaseResponse<Any>

    @PUT(ApiConstants.UPDATE_DOC)
    suspend fun updateUserDoc(@Body request: RequestBody): BaseResponse<Any>

    @GET(ApiConstants.SEARCH_VENUE)
    suspend fun getAllVenue(
        @Query("search") search: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): BaseResponse<ArrayList<VenueDetails>>

    @GET(ApiConstants.GET_ALL_OPPORTUNITIES)
    suspend fun getAllOpportunities(): BaseResponse<List<OpportunitiesItem>>

    @GET(ApiConstants.GET_OPPORTUNITY_ID)
    suspend fun getOpportunityDetail(@Query("eventId") id: String): BaseResponse<OpportunitiesDetail>

    @GET(ApiConstants.GET_FILTERS)
    suspend fun getFilters(): BaseResponse<FilterResponse>

    @GET(ApiConstants.EVENT_GET_DIVISIONS)
    suspend fun getEventDivision(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("eventId") id: String
    ): BaseResponse<List<DivisionData>>

    @POST(ApiConstants.EVENT_TEAM_REGISTRATION)
    suspend fun registerEvent(@Body request: RegisterRequest): BaseResponse<Any>

    @PUT(ApiConstants.EVENT_UPDATE_FILTERS)
    suspend fun updateFilters(@Body request: FilterUpdateRequest): BaseResponse<Any>

    @POST(ApiConstants.CREATE_NEW_EVENT)
    suspend fun createNewEvent(@Body createEventReq: CreateEventReq): BaseResponse<Any>

    @PUT(ApiConstants.ACCEPT_COACH_EVENT)
    suspend fun acceptEventInvite(@Body request: RequestBody): BaseResponse<Any>

    @PUT(ApiConstants.DECLINE_COACH_EVENT)
    suspend fun rejectEventInvite(@Body request: RequestBody): BaseResponse<Any>

    @GET(ApiConstants.GET_MY_LEAGUE)
    suspend fun getMyLeagues(
        @Query("page") page: Int, @Query("limit") limit: Int, @Query("sort") sort: String
    ): BaseResponse<ArrayList<MyLeagueResponse>>

    @GET(ApiConstants.GET_DIVISION)
    suspend fun getDivisions(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("sort") sort: String,
        @Query("leagueId") leagueId: String,
        @Query("gender") gender: String,
    ): BaseResponse<ArrayList<DivisionResponse>>

    @GET(ApiConstants.GET_VENUES)
    suspend fun getVenues(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("sort") sort: String,
        @Query("leagueId") leagueId: String,
    ): BaseResponse<VenuesResponse>

    @GET(ApiConstants.GET_EVENT_DETAILS)
    suspend fun getEventDetails(
        @Query("id") eventId: String,
    ): BaseResponse<EventDetails>


    @PUT(ApiConstants.UPDATE_EVENT_NOTE)
    suspend fun updateEventNote(@Body request: RequestBody): BaseResponse<Any>

    @GET(ApiConstants.GET_ALL_TEAMS_BY_DIVISION_AND_LEAGUES)
    suspend fun getTeamsByLeagueAndDivision(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("sort") sort: String,
        @Query("leagueId") leagueId: String,
        @Query("divisionId") divisionId: String,
    ): BaseResponse<ArrayList<TeamsByLeagueDivisionResponse>>

    @GET(ApiConstants.GET_ALL_TEAMS_BY_LEAGUE_ID_All_DIVISIONS)
    suspend fun getTeamsByLeagueIdAllDivision(
        @Query("leagueId") leagueId: String,
    ): BaseResponse<ArrayList<DivisionWiseTeamResponse>>

    @GET(ApiConstants.GET_ALL_TEAMS_STANDING_BY_LEAGUE_AND_DIVISION)
    suspend fun getAllTeamsStandingByLeaguedAndDivision(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("sort") sort: String,
        @Query("leagueId") leagueId: String,
        @Query("divisionId") divisionId: String,
    ): BaseResponse<StandingByLeagueAndDivisionData>

    @GET(ApiConstants.GET_VENUE_DETAILS_BY_ID)
    suspend fun getVenueDetailsById(
        @Query("id") venueId: String,
    ): BaseResponse<VenueDetails>

    @GET(ApiConstants.GET_EVENT_SCHEDULE)
    suspend fun getEventSchedule(
        @Query("eventId") eventId: String,
    ): BaseResponse<List<ScheduleResponse>>

    @GET(ApiConstants.SWAP_PROFILE)
    suspend fun getSwapProfiles(): BaseResponse<List<SwapUser>>

    @GET(ApiConstants.SWAP_TOKEN)
    suspend fun updateProfileToken(@Query("userId") userId: String): BaseResponse<String>

    @GET(ApiConstants.SWAP_INITIAL_TOKEN)
    suspend fun updateInitialProfileToken(@Query("userId") userId: String): BaseResponse<String>

    @GET(ApiConstants.GET_TEAM_INVITED)
    suspend fun getInviteMembersByTeamId(@Query("teamId") teamId: String): BaseResponse<List<Members>>

    @POST(ApiConstants.ADD_PROFILE)
    suspend fun addProfile(@Body request: AddProfileRequest): BaseResponse<UserInfo>

    @GET(ApiConstants.GET_CHATS)
    suspend fun getAllChats(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("sort") sort: String,
        @Query("userId") userId: String
    ): BaseResponse<ArrayList<Team>>

    @POST(ApiConstants.AUTH_REGISTER_MOBILE)
    suspend fun registerMobile(@Body signUpData: SignUpPhoneData): BaseResponse<UserPhoneInfo>

    @POST(ApiConstants.SAVE_CHAT_GROUP)
    suspend fun saveChatGroup(@Body request: RequestBody): BaseResponse<Any>

    @GET(ApiConstants.GET_SEARCH_GAME_STAFF)
    suspend fun getSearchGameStaff(@Query("search") search: String): BaseResponse<List<GetSearchStaff>>

    @POST(ApiConstants.REGISTER_GAME_STAFF)
    suspend fun registerGameStaff(@Body gameStaffRegisterRequest: GameStaffRegisterRequest): BaseResponse<Any>

}
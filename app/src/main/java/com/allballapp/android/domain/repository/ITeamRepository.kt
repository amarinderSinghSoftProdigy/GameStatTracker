package com.allballapp.android.domain.repository

import com.allballapp.android.common.ResultWrapper
import com.allballapp.android.data.request.CreateTeamRequest
import com.allballapp.android.data.request.Members
import com.allballapp.android.data.request.UpdateTeamDetailRequest
import com.allballapp.android.data.request.UpdateTeamRequest
import com.allballapp.android.data.response.*
import com.allballapp.android.data.response.homepage.HomePageCoachModel
import com.allballapp.android.data.response.invitation.InvitationData
import com.allballapp.android.data.response.roaster.RoasterResponse
import com.allballapp.android.data.response.team.Player
import com.allballapp.android.data.response.team.Team
import com.allballapp.android.domain.BaseResponse
import com.allballapp.android.ui.features.home.invitation.Invitation
import com.allballapp.android.ui.features.venue.VenueDetails
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface ITeamRepository {
    suspend fun getAllPlayers(
        page: Int = 1,
        limit: Int = 10,
        searchPlayer: String
    ): ResultWrapper<BaseResponse<ArrayList<Player>>>

    suspend fun createTeamAPI(request: CreateTeamRequest): ResultWrapper<BaseResponse<TeamData>>

    suspend fun getTeams(
        coachId: String
    ): ResultWrapper<BaseResponse<ArrayList<Team>>>

    suspend fun getTeamsUserId(
        coachId: String
    ): ResultWrapper<BaseResponse<com.allballapp.android.data.response.team.Result>>

    suspend fun getTeamsByTeamID(teamId: String): ResultWrapper<BaseResponse<Team>>

    suspend fun getLeaderBoard(teamId: String): ResultWrapper<BaseResponse<Team>>

    suspend fun getTeamsStanding(
        page: Int = 1,
        limit: Int = 10
    ): ResultWrapper<BaseResponse<StandingData>>

    suspend fun getTeamCoachPlayerByID(id: String): ResultWrapper<BaseResponse<RoasterResponse>>

    suspend fun updateTeamDetails(id: UpdateTeamDetailRequest): ResultWrapper<BaseResponse<Team>>

    suspend fun inviteMembersByTeamId(updateTeamRequest: UpdateTeamRequest): ResultWrapper<BaseResponse<InvitationData>>

    suspend fun getInviteMembersByTeamId(teamId: String): ResultWrapper<BaseResponse<List<Members>>>

    suspend fun getAllInvitation(
        page: Int = 1,
        limit: Int = 50,
        sort: String = ""
    ): ResultWrapper<BaseResponse<ArrayList<Invitation>>>


    suspend fun acceptTeamInvitation(
        invitationId: String,
        role: String,
        playerId: String,
        guardianGender: String,
    ): ResultWrapper<BaseResponse<Any>>

    suspend fun rejectTeamInvitation(invitationId: String): ResultWrapper<BaseResponse<Any>>

    suspend fun getHomePageDetails(teamId: String): ResultWrapper<BaseResponse<HomePageCoachModel>>

    suspend fun getUserRoles(role: String): ResultWrapper<BaseResponse<List<UserRoles>>>

    suspend fun getPlayerById(
        id: String,
        eventRegistration: String
    ): ResultWrapper<BaseResponse<ArrayList<PlayerDetails>>>

    suspend fun getAllVenue(
        search: String = "",
        page: Int = 1,
        limit: Int = 20
    ): ResultWrapper<BaseResponse<ArrayList<VenueDetails>>>

    suspend fun getMyLeagues(
         teamId: String,
        page: Int = 1,
        limit: Int = 20,
        sort: String = ""
    ): ResultWrapper<BaseResponse<ArrayList<MyLeagueResponse>>>

    suspend fun getDivisions(
        page: Int = 1,
        limit: Int = 20,
        sort: String = "",
        gender: String,
        leagueId: String
    ): ResultWrapper<BaseResponse<ArrayList<DivisionResponse>>>

    suspend fun getVenues(
        page: Int = 1,
        limit: Int = 20,
        sort: String = "",
        leagueId: String
    ): ResultWrapper<BaseResponse<VenuesResponse>>

}
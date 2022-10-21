package com.softprodigy.ballerapp.domain.repository

import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.data.request.CreateTeamRequest
import com.softprodigy.ballerapp.data.request.UpdateTeamDetailRequest
import com.softprodigy.ballerapp.data.request.UpdateTeamRequest
import com.softprodigy.ballerapp.data.response.*
import com.softprodigy.ballerapp.data.response.homepage.HomePageCoachModel
import com.softprodigy.ballerapp.data.response.roaster.RoasterResponse
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.data.response.team.Team
import com.softprodigy.ballerapp.data.response.team.TeamParent
import com.softprodigy.ballerapp.domain.BaseResponse
import com.softprodigy.ballerapp.ui.features.home.invitation.Invitation
import com.softprodigy.ballerapp.ui.features.venue.Venue
import javax.inject.Singleton

@Singleton
interface ITeamRepository {
    suspend fun getAllPlayers(
        page: Int = 1,
        limit: Int = 10,
        searchPlayer: String
    ): ResultWrapper<BaseResponse<ArrayList<Player>>>

    suspend fun createTeamAPI(request: CreateTeamRequest): ResultWrapper<BaseResponse<CreateTeamResponse>>

    suspend fun getTeams(
        coachId: String
    ): ResultWrapper<BaseResponse<ArrayList<Team>>>

    suspend fun getTeamsUserId(
        coachId: String
    ): ResultWrapper<BaseResponse<ArrayList<TeamParent>>>

    suspend fun getTeamsByTeamID(teamId: String): ResultWrapper<BaseResponse<Team>>

    suspend fun getLeaderBoard(teamId: String): ResultWrapper<BaseResponse<Team>>

    suspend fun getTeamsStanding(
        page: Int = 1,
        limit: Int = 10
    ): ResultWrapper<BaseResponse<StandingData>>

    suspend fun getTeamCoachPlayerByID(id: String): ResultWrapper<BaseResponse<RoasterResponse>>

    suspend fun updateTeamDetails(id: UpdateTeamDetailRequest): ResultWrapper<BaseResponse<Team>>

    suspend fun inviteMembersByTeamId(updateTeamRequest: UpdateTeamRequest): ResultWrapper<BaseResponse<Any>>

    suspend fun getInviteMembersByTeamId(teamId: String): ResultWrapper<BaseResponse<Any>>

    suspend fun getAllInvitation(
        page: Int = 1,
        limit: Int = 50,
        sort: String = ""
    ): ResultWrapper<BaseResponse<ArrayList<Invitation>>>


    suspend fun acceptTeamInvitation(
        invitationId: String,
        role: String,
        playerId: String,
        playerGender: String,
    ): ResultWrapper<BaseResponse<Any>>


    suspend fun rejectTeamInvitation(invitationId: String): ResultWrapper<BaseResponse<Any>>

    suspend fun getHomePageDetails(): ResultWrapper<BaseResponse<HomePageCoachModel>>

    suspend fun getUserRoles(role:String): ResultWrapper<BaseResponse<List<UserRoles>>>

    suspend fun getPlayerById(id: String): ResultWrapper<BaseResponse<ArrayList<PlayerDetails>>>

    suspend fun getAllVenue(
        page: Int = 1,
        limit: Int = 20
    ): ResultWrapper<BaseResponse<ArrayList<Venue>>>

    suspend fun getMyLeagues(
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
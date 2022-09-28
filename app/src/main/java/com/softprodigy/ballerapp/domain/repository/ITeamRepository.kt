package com.softprodigy.ballerapp.domain.repository

import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.data.request.CreateTeamRequest
import com.softprodigy.ballerapp.data.request.UpdateTeamDetailRequest
import com.softprodigy.ballerapp.data.request.UpdateTeamRequest
import com.softprodigy.ballerapp.data.response.CreateTeamResponse
import com.softprodigy.ballerapp.data.response.StandingData
import com.softprodigy.ballerapp.data.response.homepage.HomePageCoachModel
import com.softprodigy.ballerapp.data.response.roaster.RoasterResponse
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.data.response.team.Team
import com.softprodigy.ballerapp.domain.BaseResponse
import com.softprodigy.ballerapp.ui.features.home.events.EventsResponse
import com.softprodigy.ballerapp.ui.features.home.invitation.Invitation
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
        page: Int = 1,
        limit: Int = 5,
        sort: String = ""
    ): ResultWrapper<BaseResponse<ArrayList<Team>>>

    suspend fun getTeamsByTeamID(teamId: String): ResultWrapper<BaseResponse<Team>>

    suspend fun getLeaderBoard(teamId: String): ResultWrapper<BaseResponse<Team>>

    suspend fun getTeamsStanding(page: Int = 1, limit: Int = 10): ResultWrapper<BaseResponse<StandingData>>

    suspend fun getTeamCoachPlayerByID(id: String): ResultWrapper<BaseResponse<RoasterResponse>>

    suspend fun updateTeamDetails(id: UpdateTeamDetailRequest): ResultWrapper<BaseResponse<Team>>

    suspend fun inviteMembersByTeamId(updateTeamRequest: UpdateTeamRequest): ResultWrapper<BaseResponse<Any>>

    suspend fun getAllInvitation(
        page: Int = 1,
        limit: Int = 50,
        sort: String = ""
    ): ResultWrapper<BaseResponse<ArrayList<Invitation>>>

    suspend fun getAllevents(
        page: Int = 1,
        limit: Int = 50,
        sort: String = ""
    ): ResultWrapper<BaseResponse<EventsResponse>>

    suspend fun acceptTeamInvitation(invitationId: String, role: String): ResultWrapper<BaseResponse<Any>>

    suspend fun rejectTeamInvitation(invitationId: String): ResultWrapper<BaseResponse<Any>>

    suspend fun getHomePageDetails():ResultWrapper<BaseResponse<HomePageCoachModel>>


}
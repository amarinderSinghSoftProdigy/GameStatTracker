package com.allballapp.android.ui.features.home.teams

import com.allballapp.android.data.request.Address
import com.allballapp.android.data.request.Members
import com.allballapp.android.data.request.UpdateTeamDetailRequest
import com.allballapp.android.data.response.AllUser
import com.allballapp.android.data.response.PlayerDetails
import com.allballapp.android.data.response.team.Team
import com.allballapp.android.data.response.team.TeamLeaderBoard
import com.allballapp.android.data.response.team.TeamRoaster


data class TeamUIState(
    val allBallId: String = "",
    val userRole: String = "",
    val isLoading: Boolean = false,
    val teams: ArrayList<Team> = ArrayList(),
    val players: ArrayList<AllUser> = ArrayList(),
    val supportStaff: ArrayList<AllUser> = ArrayList(),
    val acceptPending: ArrayList<AllUser> = ArrayList(),
    val playersList: ArrayList<PlayerDetails> = ArrayList(),
    val coaches: ArrayList<AllUser> = ArrayList(),
    val allUsers: ArrayList<AllUser> = ArrayList(),
    val leaderBoard: List<TeamLeaderBoard> = emptyList(),
    //val roasterTabs: List<Player> = emptyList(),
    val roaster: List<TeamRoaster> = emptyList(),
    val selectedTeam: Team? = null,
    val showDialog: Boolean = false,
    val teamColorPrimary: String = "",

    val teamName: String = "",
    val createdBy: String = "",
    val logo: String? = null, //server
    val localLogo: String? = null, //local uri
    val search: String = "",
    //val selected: ArrayList<String> = ArrayList(),
    val checked: Boolean = false,
    val updatedTeam: UpdateTeamDetailRequest = UpdateTeamDetailRequest(),
    val all: Boolean = false,

    val teamColorSec: String = "",
    val teamColorThird: String = "",
    val teamNameOnJerseys: String = "",
    val teamNameOnTournaments: String = "",
    val venueName: String = "",
    val selectedAddress: Address = Address(),
    val loadFirstUi: Boolean = false,
    val teamId: String = "",
    val newTeamId: String = "",
    val member: Members = Members()
)

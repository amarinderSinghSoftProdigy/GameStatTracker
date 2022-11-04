package com.allballapp.android.ui.features.home.teams.chat

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.allballapp.android.data.request.Address
import com.allballapp.android.data.request.UpdateTeamDetailRequest
import com.allballapp.android.data.response.PlayerDetails
import com.allballapp.android.data.response.team.*


data class ChatUIState(
    val selectedPlayers: ArrayList<Player> = ArrayList(),
    val selectedCoaches: ArrayList<Coach> = ArrayList(),
    val teamIndex: Int = 0,
    val userRole: String = "",
    val isLoading: Boolean = false,
    val teams: ArrayList<Team> = ArrayList(),
    val playersList: ArrayList<PlayerDetails> = ArrayList(),
    val leaderBoard: List<TeamLeaderBoard> = emptyList(),
    //val roasterTabs: List<Player> = emptyList(),
    val roaster: List<TeamRoaster> = emptyList(),
    val selectedTeam: Team? = null,
    val showDialog: Boolean = false,
    val teamColorPrimary: String = "",

    val teamName: String = "",
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


    /*New conversation states*/
    val selectedPlayersForNewGroup:SnapshotStateList<String> = mutableStateListOf(),
    val selectedCoachesForNewGroup:SnapshotStateList<String> = mutableStateListOf(),
    val showCreateGroupNameDialog : Boolean = false,
    val groupName : String = ""
    )

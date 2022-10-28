package com.allballapp.android.ui.features.user_type.team_setup.updated

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.allballapp.android.data.request.Address
import com.allballapp.android.data.request.Members
import com.allballapp.android.data.response.UserRoles
import com.allballapp.android.data.response.team.Player

data class TeamSetupUIStateUpdated(
    val teamColorPrimary: String = "",
    val teamColorSec: String = "",
    val teamColorThird: String = "",
    val teamName: String = "",
    val teamImageUri: String? = null,
    val teamImageServerUrl: String = "",
    val roles: List<UserRoles> = mutableListOf(),

    val isLoading: Boolean = false,
    val players: ArrayList<Player> = ArrayList(),
    val selectedPlayers: ArrayList<Player> = ArrayList(),
    val invitedPlayers: ArrayList<Player> = ArrayList(),
    val search: String = "",

    val showDialog: Boolean = false,
    val removePlayer: Player? = null,

    /* Initialising default value with three*/
    //val inviteMemberName: ArrayList<String> = arrayListOf("", "", "", "", ""),
    //val inviteMemberEmail: ArrayList<String> = arrayListOf("", "", "", "", ""),
    //var inviteMemberCount: Int = 5,
    var inviteList: SnapshotStateList<InviteObject> = mutableStateListOf(),
    var memberList: List<Members> = mutableListOf(),

    val teamNameOnJerseys: String = "",
    val teamNameOnTournaments: String = "",
    val venueName: String = "",

    val index: Int = 0,
    val coachName: String = "",
    val coachRole: String = "",
    val coachEmail: String = "",
    val selectedAddress: Address = Address(),
)


data class InviteObject(
    var name: String = "",
    var contact: String = "",
    var role: UserRoles = UserRoles(),
    var countryCode: String = "",
)
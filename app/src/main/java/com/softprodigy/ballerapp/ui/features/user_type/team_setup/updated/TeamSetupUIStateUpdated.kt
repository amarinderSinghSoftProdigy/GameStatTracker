package com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated

import com.softprodigy.ballerapp.data.request.Address
import com.softprodigy.ballerapp.data.response.team.Player

data class TeamSetupUIStateUpdated(
    val teamColorPrimary: String = "",
    val teamColorSec: String = "",
    val teamColorThird: String = "",
    val teamName: String = "",
    val teamImageUri: String? = null,
    val teamImageServerUrl: String = "",

    val isLoading: Boolean = false,
    val players: ArrayList<Player> = ArrayList(),
    val selectedPlayers: ArrayList<Player> = ArrayList(),
    val search: String = "",

    val showDialog: Boolean = false,
    val removePlayer: Player? = null,

    /* Initialising default value with three*/
    //val inviteMemberName: ArrayList<String> = arrayListOf("", "", "", "", ""),
    //val inviteMemberEmail: ArrayList<String> = arrayListOf("", "", "", "", ""),
    //var inviteMemberCount: Int = 5,
    var inviteList: List<InviteObject> = mutableListOf(),
    var teamInviteList: ArrayList<String> = arrayListOf("", "", ""),

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
)
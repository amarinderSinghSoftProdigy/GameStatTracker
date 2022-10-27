package com.softprodigy.ballerapp.data.response.team

import com.google.gson.annotations.SerializedName
import com.softprodigy.ballerapp.data.request.Address
import com.softprodigy.ballerapp.data.request.Location
import com.softprodigy.ballerapp.data.response.SwapUser
import com.softprodigy.ballerapp.data.response.TeamId

data class TeamParent(
    @SerializedName("_id")
    val _id: String = "",
    @SerializedName("role")
    val role: String = "",
    @SerializedName("teamId")
    val teamId: Team = Team(),
)

data class Result(@SerializedName("result") val result: ArrayList<TeamParent> = arrayListOf())

data class Team(
    @SerializedName("_id")
    val _id: String = "",
    @SerializedName("coachDetails")
    val coaches: ArrayList<Coach> = ArrayList(),
    @SerializedName("colorCode")
    val colorCode: String = "",
    @SerializedName("logo")
    val logo: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("playerDetails")
    val players: ArrayList<Player> = ArrayList(),
    @SerializedName("supportingCastDetails")
    val supportingCastDetails: ArrayList<SwapUser> = ArrayList(),
    @SerializedName("pendingAndDeclinedMembers")
    val pendingAndDeclinedMembers: ArrayList<SwapUser> = ArrayList(),
    @SerializedName("leaderboardPoints")
    val teamLeaderBoard: ArrayList<TeamLeaderBoard> = ArrayList(),
    @SerializedName("teamChatGroups")
    val teamChatGroups: ArrayList<TeamChatGroup> = ArrayList(),

    @SerializedName("teamNameOnJersey") val teamNameOnJersey: String = "",
    @SerializedName("teamNameOnTournaments") val teamNameOnTournaments: String = "",
    @SerializedName("primaryTeamColor") val primaryTeamColor: String = "",
    @SerializedName("secondaryTeamColor") val secondaryTeamColor: String = "",
    @SerializedName("tertiaryTeamColor") val tertiaryTeamColor: String = "",
    @SerializedName("nameOfVenue") val nameOfVenue: String = "",
    @SerializedName("location") val location: Location = Location(),
    @SerializedName("address") val address: Address = Address(),
)

data class TeamChatGroup(
    @SerializedName("_id"     ) val Id      : String = "",
    @SerializedName("teamId"  ) val teamId  : String = "",
    @SerializedName("groupId" ) val groupId : String = ""
)
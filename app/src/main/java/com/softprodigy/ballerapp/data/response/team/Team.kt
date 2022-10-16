package com.softprodigy.ballerapp.data.response.team

import com.google.gson.annotations.SerializedName
import com.softprodigy.ballerapp.data.request.Address
import com.softprodigy.ballerapp.data.request.Location

data class TeamParent(
    @SerializedName("_id")
    val _id: String = "",
    @SerializedName("teamId")
    val teamId: Team = Team(),
)

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
    @SerializedName("leaderboardPoints")
    val teamLeaderBoard: ArrayList<TeamLeaderBoard> = ArrayList(),
    @SerializedName("teamChatGroups")
    val teamChatGroups: ArrayList<Any> = ArrayList(),

    @SerializedName("teamNameOnJersey") val teamNameOnJersey: String = "",
    @SerializedName("teamNameOnTournaments") val teamNameOnTournaments: String = "",
    @SerializedName("primaryTeamColor") val primaryTeamColor: String = "",
    @SerializedName("secondaryTeamColor") val secondaryTeamColor: String = "",
    @SerializedName("tertiaryTeamColor") val tertiaryTeamColor: String = "",
    @SerializedName("nameOfVenue") val nameOfVenue: String = "",
    @SerializedName("location") val location: Location = Location(),
    @SerializedName("address") val address: Address = Address(),
)
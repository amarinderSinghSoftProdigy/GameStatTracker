package com.softprodigy.ballerapp.data.request

import com.google.gson.annotations.SerializedName
import com.softprodigy.ballerapp.data.response.team.TeamLeaderBoard
import com.softprodigy.ballerapp.data.response.team.TeamRoaster

data class CreateTeamRequest(

    @SerializedName("name") val name: String = "",
    @SerializedName("primaryTeamColor") val primaryTeamColor: String = "",
    @SerializedName("colorCode") val colorCode: String = "",
    @SerializedName("secondaryTeamColor") val secondaryTeamColor: String = "",
    @SerializedName("tertiaryTeamColor") val tertiaryTeamColor: String = "",
    @SerializedName("members") val members: List<Members> = arrayListOf(),
    @SerializedName("logo") val logo: String? = null
)

data class UpdateTeamRequest(
    @SerializedName("teamID") val teamID: String = "",
    @SerializedName("members") val members: List<Members> = arrayListOf(),
)

data class UpdateTeamDetailRequest(
    @SerializedName("id") val id: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("colorCode") val colorCode: String = "",
    @SerializedName("primaryTeamColor") val primaryTeamColor: String = "",
    @SerializedName("logo") val logo: String = "",
    @SerializedName("leaderboardPoints") val leaderboardPoints: List<TeamLeaderBoard> = arrayListOf(),
    @SerializedName("playerPositions") val playerPositions: List<TeamRoaster> = arrayListOf()
)


data class Members(
    @SerializedName("name") var name: String? = null,
    @SerializedName("email") var email: String? = null
)
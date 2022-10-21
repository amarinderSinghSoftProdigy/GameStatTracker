package com.softprodigy.ballerapp.data.request

import com.google.gson.annotations.SerializedName
import com.softprodigy.ballerapp.data.response.team.TeamLeaderBoard
import com.softprodigy.ballerapp.data.response.team.TeamRoaster

data class CreateTeamRequest(

    @SerializedName("name") val name: String = "",
    @SerializedName("teamNameOnJersey") val teamNameOnJersey: String = "",
    @SerializedName("teamNameOnTournaments") val teamNameOnTournaments: String = "",
    @SerializedName("nameOfVenue") val nameOfVenue: String = "",
    @SerializedName("address") val address: Address = Address(),
    @SerializedName("location") val location: Location = Location(),
    @SerializedName("primaryTeamColor") val primaryTeamColor: String = "",
    @SerializedName("colorCode") val colorCode: String = "",
    @SerializedName("secondaryTeamColor") val secondaryTeamColor: String = "",
    @SerializedName("tertiaryTeamColor") val tertiaryTeamColor: String = "",
    @SerializedName("members") val members: List<Members> = arrayListOf(),
    @SerializedName("logo") val logo: String? = null,
)

data class UpdateTeamRequest(
    @SerializedName("teamID") val teamID: String = "",
    @SerializedName("userType") val userType: String = "",
    @SerializedName("profilesSelected") val profilesSelected: Boolean = false,
    @SerializedName("type") val type: String = "",
    @SerializedName("members") val members: List<Members> = arrayListOf(),
)

data class UpdateTeamDetailRequest(
    @SerializedName("id") val id: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("colorCode") val colorCode: String = "",
    @SerializedName("primaryTeamColor") val primaryTeamColor: String = "",
    @SerializedName("logo") val logo: String = "",
    @SerializedName("leaderboardPoints") val leaderboardPoints: List<TeamLeaderBoard> = arrayListOf(),
    @SerializedName("playerPositions") val playerPositions: List<TeamRoaster> = arrayListOf(),

    @SerializedName("nameOfVenue") val nameOfVenue: String = "",
    @SerializedName("secondaryTeamColor") val secondaryTeamColor: String = "",
    @SerializedName("tertiaryTeamColor") val tertiaryTeamColor: String = "",
    @SerializedName("teamNameOnJersey") val teamNameOnJersey: String = "",
    @SerializedName("teamNameOnTournaments") val teamNameOnTournaments: String = "",
    @SerializedName("address") val address: Address = Address(),
    @SerializedName("location") val location: Location = Location(),
)


data class Members(
    @SerializedName("name") var name: String = "",
    @SerializedName("mobileNumber") var mobileNumber: String = "", /*need to send profile _id with mobileNumber field in case of invitation from select swap profile dialog */
    @SerializedName("role") var role: String = "",
)
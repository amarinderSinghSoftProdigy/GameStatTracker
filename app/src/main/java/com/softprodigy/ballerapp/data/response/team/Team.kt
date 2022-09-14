package com.softprodigy.ballerapp.data.response.team

import com.google.gson.annotations.SerializedName

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
    val teamLeaderBoard: ArrayList<TeamLeaderBoard> = ArrayList()
)
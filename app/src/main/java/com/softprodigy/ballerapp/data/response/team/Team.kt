package com.softprodigy.ballerapp.data.response.team

import com.google.gson.annotations.SerializedName
import com.softprodigy.ballerapp.data.response.team.Coach
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.data.response.team.TeamLeaderBoard

data class Team(
    @SerializedName("_id")
    val _id: String = "",
    @SerializedName("coachDetails")
    val coaches: List<Coach> = emptyList(),
    @SerializedName("colorCode")
    val colorCode: String = "",
    @SerializedName("logo")
    val logo: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("playerDetails")
    val players: List<Player> = emptyList(),
    @SerializedName("leaderboardPoints")
    val     teamLeaderBoard: List<TeamLeaderBoard> = emptyList()
)
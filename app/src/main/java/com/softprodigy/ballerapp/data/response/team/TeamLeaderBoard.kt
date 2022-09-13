package com.softprodigy.ballerapp.data.response.team

import com.google.gson.annotations.SerializedName

data class TeamLeaderBoard(
    @SerializedName("__v")
    val __v: Int? = null,
    @SerializedName("_id")
    val _id: String? = "",
    @SerializedName("createdAt")
    val createdAt: String? = "",
    @SerializedName("createdBy")
    val createdBy: String? = "",
    @SerializedName("leaderboardPoints")
    val leaderboardPoints: List<LeaderboardPoint> = emptyList(),
    @SerializedName("teamId")
    val teamId: String? = "",
    @SerializedName("updatedAt")
    val updatedAt: String? = ""
)
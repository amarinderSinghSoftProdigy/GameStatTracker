package com.allballapp.android.data.response

import com.google.gson.annotations.SerializedName
import com.allballapp.android.data.response.team.Coach
import com.allballapp.android.data.response.team.Player

data class Team(
    @SerializedName("_id") val Id: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("position") val position: String = "",
    @SerializedName("jerseynNo") val jerseynNo: String = "",
    @SerializedName("role") val role: String = "",
    @SerializedName("divisions") val divisions: ArrayList<String> = arrayListOf(),
    @SerializedName("events") val events: ArrayList<String> = arrayListOf(),
    @SerializedName("logo") val logo: String = "",
    @SerializedName("primaryTeamColor") val primaryTeamColor: String = "",
    @SerializedName("secondaryTeamColor") val secondaryTeamColor: String = "",
    @SerializedName("tertiaryTeamColor") val tertiaryTeamColor: String = "",
    @SerializedName("coaches") val coaches: ArrayList<Coach> = arrayListOf(),
    @SerializedName("players") val players: ArrayList<Player> = arrayListOf(),
    @SerializedName("status") val status: String = "",
    @SerializedName("isDelete") val isDelete: Boolean = false,
    @SerializedName("createdBy") val createdBy: String = "",
    @SerializedName("createdAt") val createdAt: String = "",
    @SerializedName("updatedAt") val updatedAt: String? = "",
    @SerializedName("__v") val _v: Int? = null,
)
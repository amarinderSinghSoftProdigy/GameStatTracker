package com.allballapp.android.data.response.team

import com.google.gson.annotations.SerializedName

data class TeamsByLeagueDivisionResponse(
    @SerializedName("_id") val Id: String = "",
    @SerializedName("team") val team: Team = Team(),
)
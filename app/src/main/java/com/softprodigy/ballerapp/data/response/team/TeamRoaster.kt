package com.softprodigy.ballerapp.data.response.team

import com.google.gson.annotations.SerializedName

data class TeamRoaster(
    @SerializedName("userId")
    val userId: String = "",
    @SerializedName("position")
    val position: String = ""
)
package com.softprodigy.ballerapp.data.response.team

import com.google.gson.annotations.SerializedName

data class LeaderboardPoint(

    @SerializedName("status")
    val status: Boolean = false,
    @SerializedName("name")
    val name: String = ""
)
package com.allballapp.android.data.response.team

import com.google.gson.annotations.SerializedName

data class TeamLeaderBoard(
    @SerializedName("status")
    var status: Boolean = false,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("key")
    val key: String = ""
)
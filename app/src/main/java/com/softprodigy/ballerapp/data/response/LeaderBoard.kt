package com.softprodigy.ballerapp.data.response

import com.google.gson.annotations.SerializedName

data class LeaderBoard(
    @SerializedName("_id") val _id: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("logo") val logo: String = "",
    @SerializedName("points") val points: String = "",
)

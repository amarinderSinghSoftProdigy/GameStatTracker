package com.softprodigy.ballerapp.data.response

import com.google.gson.annotations.SerializedName

data class GameSummaryResponse(
    @SerializedName("_id") val _id: String = "",
    @SerializedName("time") val time: String = "",
    @SerializedName("playerDp") val playerDp: String = "",
    @SerializedName("playerName") val playerName: String = "",
    @SerializedName("desc") val desc: String = "",
    @SerializedName("value") val value: String = "",
)


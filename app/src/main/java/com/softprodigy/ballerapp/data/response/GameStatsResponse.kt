package com.softprodigy.ballerapp.data.response

import com.google.gson.annotations.SerializedName

data class GameStatsResponse(
    @SerializedName("_id") val _id: String = "",
    @SerializedName("player") val player: String = "",
    @SerializedName("pts") val pts: String = "",
    @SerializedName("fouls") val fouls: String = "",
    @SerializedName("fg") val fg: String = "",
    @SerializedName("3pt") val threept: String = "",
    @SerializedName("ft") val ft: String = "",
    @SerializedName("rbnd") val rbnd: String = "",
    @SerializedName("asst") val asst: String = "",
    @SerializedName("to") val to: String = "",
) {
    operator fun iterator(): List<String> {
        return listOf(player,pts,fouls,fg,threept,ft,rbnd,asst,to)
    }
}

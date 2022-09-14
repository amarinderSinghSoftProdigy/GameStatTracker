package com.softprodigy.ballerapp.data.response

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.softprodigy.ballerapp.data.response.team.Player

data class StandingData(
    @SerializedName("categories")
    val categories: List<String> = emptyList(),
    @SerializedName("teamsStandings")
    val teamsStandings: List<Standing> = emptyList(),
)

data class Standing(
    @SerializedName("_id") val _id: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("logo") val logo: String = "",
    @SerializedName("standings") val standings: String = "",
    @SerializedName("WinPer") val WinPer: String = "",
    @SerializedName("GB") val GB: String = "",
    @SerializedName("Home") val Home: String = "",
    @SerializedName("Away") val Away: String = "",
    @SerializedName("PF") val PF: String = "",
) {
    override fun toString(): String {
        var gson = Gson();
        var json = gson.toJson(this, Standing::class.java);
        return json;
    }
}


package com.allballapp.android.data.response

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class StandingData(
    @SerializedName("categories")
    val categories: List<Categories> = emptyList(),
    @SerializedName("teamsStandings")
    val teamsStandings: List<Standing> = emptyList(),
)

data class StandingByLeagueAndDivisionData(
    @SerializedName("categories") val categories: ArrayList<Categories> = arrayListOf(),
    @SerializedName("allTeams") val allTeams: AllTeams = AllTeams()

)

data class AllTeams(
    @SerializedName("_id") val Id: String = "",
    @SerializedName("divisionName") val divisionName: String = "",
    @SerializedName("registeredTeams") val registeredTeams: ArrayList<Standing> = arrayListOf()
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


data class Categories (
    @SerializedName("name" ) val name : String = "",
    @SerializedName("key"  ) val key  : String = ""
)


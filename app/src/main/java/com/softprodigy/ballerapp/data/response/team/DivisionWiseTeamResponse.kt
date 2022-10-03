package com.softprodigy.ballerapp.data.response.team

import com.google.gson.annotations.SerializedName

data class DivisionWiseTeamResponse(
    @SerializedName("_id") val Id: String = "",
    @SerializedName("divisionName") val divisionName: String = "",
    @SerializedName("registeredTeams") val team: ArrayList<Team> = arrayListOf()
)

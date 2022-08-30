package com.softprodigy.ballerapp.data.request

import com.google.gson.annotations.SerializedName

data class CreateTeamRequest(
    @SerializedName("name") val name: String = "",
    @SerializedName("colorCode") val colorCode: String = "",
    @SerializedName("players") val players: ArrayList<String> = arrayListOf(),
    @SerializedName("coaches") val coaches: ArrayList<String> = arrayListOf(),
    @SerializedName("logo") val logo: String = ""
)
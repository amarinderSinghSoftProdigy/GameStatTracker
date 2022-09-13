package com.softprodigy.ballerapp.data.request

import com.google.gson.annotations.SerializedName

data class CreateTeamRequest(

    @SerializedName("name") val name: String = "",
    @SerializedName("primaryTeamColor") val primaryTeamColor: String = "",
    @SerializedName("secondaryTeamColor") val secondaryTeamColor: String = "",
    @SerializedName("tertiaryTeamColor") val tertiaryTeamColor: String = "",
    @SerializedName("members") val members: List<Members> = arrayListOf(),
    @SerializedName("logo") val logo: String? = null
)

data class UpdateTeamRequest(
    @SerializedName("teamID") val teamID: String = "",
    @SerializedName("members") val members: List<Members> = arrayListOf(),
)


data class Members(
    @SerializedName("name") var name: String? = null,
    @SerializedName("email") var email: String? = null
)
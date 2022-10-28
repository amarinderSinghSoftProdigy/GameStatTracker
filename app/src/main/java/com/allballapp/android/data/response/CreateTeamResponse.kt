package com.allballapp.android.data.response

import com.google.gson.annotations.SerializedName

data class CreateTeamResponse(
    @SerializedName("name") val name: String = "",
    @SerializedName("divisions") val divisions: ArrayList<String> = arrayListOf(),
    @SerializedName("events") var events: ArrayList<String> = arrayListOf(),
    @SerializedName("logo") var logo: String = "",
    @SerializedName("colorCode") var colorCode: String = "",
    @SerializedName("coaches") var coaches: ArrayList<String> = arrayListOf(),
    @SerializedName("players") var players: ArrayList<String> = arrayListOf(),
    @SerializedName("status") var status: String = "",
    @SerializedName("isDelete") var isDelete: Boolean = false,
    @SerializedName("createdBy") var createdBy: String = "",
    @SerializedName("_id") var Id: String = "",
    @SerializedName("createdAt") var createdAt: String = "",
    @SerializedName("updatedAt") var updatedAt: String = "",
    @SerializedName("__v") var _v: Int? = null
)
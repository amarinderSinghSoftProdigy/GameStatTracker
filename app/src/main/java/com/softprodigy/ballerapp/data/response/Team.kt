package com.softprodigy.ballerapp.data.response

import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class Team(
    @SerializedName("_id") val Id: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("divisions") val divisions: ArrayList<String> = arrayListOf(),
    @SerializedName("events") val events: ArrayList<String> = arrayListOf(),
    @SerializedName("logo") val logo: String = "",
    @SerializedName("colorCode") val colorCode: String = "",
    @SerializedName("coaches") val coaches: ArrayList<Coach> = arrayListOf(),
    @SerializedName("players") val players: ArrayList<Player> = arrayListOf(),
    @SerializedName("status") val status: String = "",
    @SerializedName("isDelete") val isDelete: Boolean = false,
    @SerializedName("createdBy") val createdBy: String = "",
    @SerializedName("createdAt") val createdAt: String = "",
    @SerializedName("updatedAt") val updatedAt: String? = "",
    @SerializedName("__v") val _v: Int? = null,
)
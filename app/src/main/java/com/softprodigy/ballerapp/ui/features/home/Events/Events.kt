package com.softprodigy.ballerapp.ui.features.home.Events

import com.google.gson.annotations.SerializedName
import com.softprodigy.ballerapp.data.response.User
import com.softprodigy.ballerapp.data.response.team.Team

data class Events(
    @SerializedName("_id") val id: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("venue") val venue: String = "",
    @SerializedName("date") val date: String = "",
    @SerializedName("status") val status: String = "",
    @SerializedName("type") val type: String = "",
    @SerializedName("backgroundColor") val backgroundColor: String = "",
)

enum class EventStatus(val status: String) {
    ACCEPT("Going"),
    REJECT("Reject"),
    PENDING("Pending"),
    PAST("Past"),
}
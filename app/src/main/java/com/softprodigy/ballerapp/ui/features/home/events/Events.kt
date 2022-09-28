package com.softprodigy.ballerapp.ui.features.home.events

import androidx.compose.ui.graphics.Color
import com.google.gson.annotations.SerializedName
import com.softprodigy.ballerapp.ui.theme.ColorButtonGreen
import com.softprodigy.ballerapp.ui.theme.ColorButtonRed
import com.softprodigy.ballerapp.ui.theme.ColorMainPrimary
import com.softprodigy.ballerapp.ui.theme.GreenColor
import com.softprodigy.ballerapp.ui.theme.Yellow700

data class Events(
    @SerializedName("_id") val id: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("venue") val venue: String = "",
    @SerializedName("date") val date: String = "",
    @SerializedName("status") val status: String = "",
    @SerializedName("type") val type: EventType? = null,
//    @SerializedName("backgroundColor") val backgroundColor: String = "",
)

enum class EventStatus(val status: String) {
    ACCEPT("Going"),
    REJECT("Reject"),
    PENDING("Pending"),
    PAST("Past"),
}

enum class EventType(val type: String, val color: Color) {
    PRACTICE("Practice", ColorButtonGreen),
    GAME("Game", ColorMainPrimary),
    ACTIVITY("Activity", Yellow700),
    SCRIMMAGE("Scrimmage", ColorButtonRed),
}
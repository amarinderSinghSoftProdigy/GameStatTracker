package com.softprodigy.ballerapp.ui.features.home.events

import androidx.compose.ui.graphics.Color
import com.google.gson.annotations.SerializedName
import com.softprodigy.ballerapp.ui.theme.ColorButtonGreen
import com.softprodigy.ballerapp.ui.theme.ColorButtonRed
import com.softprodigy.ballerapp.ui.theme.ColorMainPrimary
import com.softprodigy.ballerapp.ui.theme.Yellow700

data class EventsResponse(
    @SerializedName("upcommingEvents") val upcommingEvents: ArrayList<Events> = arrayListOf(),
    @SerializedName("pastEvents") val pastEvents: ArrayList<Events> = arrayListOf(),
)
data class Events(
    @SerializedName("_id") val id: String = "",
    @SerializedName("eventName") val eventName: String = "",
    @SerializedName("landmarkLocation") val landmarkLocation: String = "",
    @SerializedName("date") val date: String = "",
    @SerializedName("invitationStatus") val invitationStatus: String = "",
    @SerializedName("reason") val reason: String="",
    @SerializedName("eventType") val eventType: String = "",
    @SerializedName("notGoing") var notGoing: ArrayList<NotGoing> = arrayListOf(),
    @SerializedName("going") var going: ArrayList<String> = arrayListOf(),
    @SerializedName("mayBe") var maybe: ArrayList<String> = arrayListOf(),

    )

enum class EventStatus(val status: String) {
    ACCEPT("Going"),
    REJECT("Not Going"),
    PENDING("Maybe"),
    PAST("Past"),
}

enum class EventType(val type: String, val color: Color) {
    PRACTICE("Practice", ColorButtonGreen),
    GAME("Game", ColorMainPrimary),
    ACTIVITY("Activity", Yellow700),
    SCRIMMAGE("Scrimmage", ColorButtonRed),
}

data class NotGoing(
    @SerializedName("id") val id: String = "",
    @SerializedName("reason") val reason: String = "",
    @SerializedName("_id") val _id: String? = ""
)
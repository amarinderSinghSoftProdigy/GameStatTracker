package com.softprodigy.ballerapp.ui.features.home.events

import androidx.compose.ui.graphics.Color
import com.google.gson.annotations.SerializedName
import com.softprodigy.ballerapp.ui.theme.ColorButtonGreen
import com.softprodigy.ballerapp.ui.theme.ColorButtonRed
import com.softprodigy.ballerapp.ui.theme.ColorMainPrimary
import com.softprodigy.ballerapp.ui.theme.GreenColor
import com.softprodigy.ballerapp.ui.theme.Yellow700

data class EventsResponse(
    @SerializedName("upcommingEvents") val upcommingEvents: ArrayList<Events> = arrayListOf(),
    @SerializedName("pastEvents") val pastEvents: ArrayList<Events> = arrayListOf(),
)
data class Events(
    @SerializedName("_id") val id: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("venue") val venue: String = "",
    @SerializedName("date") val date: String = "",
    @SerializedName("status") val status: String = "",
    @SerializedName("type") val type: EventType? = null,
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

data class FilterResponse(
    @SerializedName("_id") val id: String = "",
    @SerializedName("userId") val userId: String = "",
    @SerializedName("filterPreferences") val filterPreferences: List<FilterPreference> = mutableListOf(),
)

data class FilterPreference(
    @SerializedName("name") val name: String = "",
    @SerializedName("status") var status: Boolean = false,
    @SerializedName("key") val key: String = "",
)

data class OpportunitiesItem(
    @SerializedName("_id") val id: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("logo") val logo: String = "",
    @SerializedName("eventType") val eventType: String = "",
    @SerializedName("startDate") val startDate: String = "",
    @SerializedName("endDate") val endDate: String = "",
    @SerializedName("standardPrice") val standardPrice: String = "",
    @SerializedName("locationDesc") val locationDesc: String = "",
    @SerializedName("eventShortDescription") val eventShortDescription: String = "",
)

data class OpportunitiesDetail(
    @SerializedName("_id") val id: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("logo") val logo: String = "",
    @SerializedName("eventType") val eventType: String = "",
    @SerializedName("startDate") val startDate: String = "",
    @SerializedName("endDate") val endDate: String = "",
    @SerializedName("standardPrice") val standardPrice: String = "",
    @SerializedName("locationDesc") val locationDesc: String = "",
    @SerializedName("eventShortDescription") val eventShortDescription: String = "",
    @SerializedName("registrationDeadline") val registrationDeadline: String = "",
    @SerializedName("address") val address: String = "",
    @SerializedName("city") val city: String = "",
    @SerializedName("state") val state: String = "",
    @SerializedName("eventGeneralInfo") val eventGeneralInfo: String = "",
    @SerializedName("userId") val userId: UserData = UserData(),
    @SerializedName("potentialDaysOfPlay") val potentialDaysOfPlay: List<DaysOfPlay> = mutableListOf(),
    @SerializedName("skillLevel") val skillLevel: List<String> = mutableListOf(),
    @SerializedName("participation") val participation: Participation = Participation(),
    @SerializedName("location") val location: LocationData = LocationData(),
)

data class DaysOfPlay(
    @SerializedName("day") val day: String = "",
    @SerializedName("earliestStartTime") val earliestStartTime: String = "",
    @SerializedName("latestStartTime") val latestStartTime: String = "",
    @SerializedName("_id") val _id: String = "",
)

data class UserData(
    @SerializedName("_id") val _id: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("phone") val phone: String = "",
)

data class Participation(
    @SerializedName("type") val type: String = "",
    @SerializedName("boysMin") val boysMin: String = "",
    @SerializedName("boysMax") val boysMax: String = "",
    @SerializedName("girlsMin") val girlsMin: String = "",
    @SerializedName("girlsMax") val girlsMax: String = "",
)

data class LocationData(
    @SerializedName("type") val type: String = "",
    //@SerializedName("coordinates") val coordinates: Any,
)

data class DivisionData(
    @SerializedName("_id") val _id: String = "",
    @SerializedName("eventId") val eventId: String = "",
    @SerializedName("divisionName") val divisionName: String = "",
)

data class RegisterRequest(
    val team: String = "",
    val event: String = "",
    val players: ArrayList<String> = ArrayList(),
    val paymentOption: String = "cash",
    val payment: String = "",
    val division: String = "",
    val sendPushNotification: Boolean = false,
    val termsAndCondition: Boolean = false,
    val privacy: Boolean = false,
)

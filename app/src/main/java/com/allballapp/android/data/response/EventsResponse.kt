package com.allballapp.android.ui.features.home.events

import androidx.compose.ui.graphics.Color
import com.allballapp.android.data.request.Address
import com.allballapp.android.data.request.Location
import com.allballapp.android.data.response.Team
import com.allballapp.android.ui.theme.ColorButtonGreen
import com.allballapp.android.ui.theme.ColorButtonRed
import com.allballapp.android.ui.theme.ColorMainPrimary
import com.allballapp.android.ui.theme.Yellow700
import com.google.gson.annotations.SerializedName

data class EventsResponse(
    @SerializedName("upcommingEvents") val upcommingEvents: ArrayList<Events> = arrayListOf(),
    @SerializedName("pastEvents") val pastEvents: ArrayList<Events> = arrayListOf(),
    @SerializedName("publishedGames") val publishedGames: ArrayList<PublishedGames> = arrayListOf(),
)

data class Events(
    @SerializedName("_id") val id: String = "",
    @SerializedName("createdBy") val createdBy: String = "",
    @SerializedName("eventName") val eventName: String = "",
    @SerializedName("landmarkLocation") val landmarkLocation: String = "",
    @SerializedName("date") val date: String = "",
    @SerializedName("invitationStatus") val invitationStatus: String = "",
    @SerializedName("reason") val reason: String = "",
    @SerializedName("eventType") val eventType: String = "",
    @SerializedName("arrivalTime") val arrivalTime: String = "",
    @SerializedName("startTime") val startTime: String = "",
    @SerializedName("endTime") val endTime: String = "",
    @SerializedName("prePractice") val prePractice: String = "",
    @SerializedName("postPractice") val postPractice: String = "",
    @SerializedName("address") val address: Address = Address(),
    @SerializedName("notGoing") val notGoing: ArrayList<NotGoing> = arrayListOf(),
    @SerializedName("going") val going: ArrayList<String> = arrayListOf(),
    @SerializedName("mayBe") val maybe: ArrayList<String> = arrayListOf(),
    @SerializedName("invites") val invites: ArrayList<Invites> = arrayListOf(),
)

data class EventDetails(
    @SerializedName("_id") val id: String = "",
    @SerializedName("createdBy") val createdBy: String = "",
    @SerializedName("eventName") val eventName: String = "",
    @SerializedName("landmarkLocation") val landmarkLocation: String = "",
    @SerializedName("date") val date: String = "",
    @SerializedName("invitationStatus") val invitationStatus: String = "",
    @SerializedName("reason") val reason: String = "",
    @SerializedName("eventType") val eventType: String = "",
    @SerializedName("arrivalTime") val arrivalTime: String = "",
    @SerializedName("startTime") val startTime: String = "",
    @SerializedName("endTime") val endTime: String = "",
    @SerializedName("prePractice") val prePractice: String = "",
    @SerializedName("postPractice") val postPractice: String = "",
    @SerializedName("address") val address: Address = Address(),
    @SerializedName("notGoing") val notGoing: ArrayList<NotGoing> = arrayListOf(),
    @SerializedName("going") val going: ArrayList<String> = arrayListOf(),
    @SerializedName("mayBe") val maybe: ArrayList<String> = arrayListOf(),
    @SerializedName("invites") val invites: ArrayList<Invites> = arrayListOf(),
    @SerializedName("coachId") val coachId: CoachId = CoachId(),
    @SerializedName("jerseyColor") val jerseyColor: String = "",
    @SerializedName("currentDate") val serverDate: String = "",
)


enum class EventStatus(val status: String) {
    GOING("Going"),
    NOT_GOING("Not Going"),
    MAY_BE("Maybe"),
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

data class FilterResponse(
    @SerializedName("_id") val id: String = "",
    @SerializedName("userId") val userId: String = "",
    @SerializedName("filterPreferences") val filterPreferences: List<FilterPreference> = mutableListOf(),
)

data class FilterUpdateRequest(
    val filterPreferences: List<FilterPreference> = mutableListOf(),
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
    @SerializedName("gender") val gender: ArrayList<String> = arrayListOf(),
    @SerializedName("format") val format: String = "",
    @SerializedName("participation") val participation: Participation = Participation()
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
    @SerializedName("userId") val userId: UserData? = UserData(),
    @SerializedName("potentialDaysOfPlay") val potentialDaysOfPlay: List<DaysOfPlay> = mutableListOf(),
    @SerializedName("skillLevel") val skillLevel: List<String> = mutableListOf(),
    @SerializedName("participation") val participation: Participation = Participation(),
    @SerializedName("registration") val registration: Boolean = false,
    @SerializedName("location") val location: Location = Location(),
    @SerializedName("basePay") val basePay: ArrayList<BasePay> = arrayListOf(),
    @SerializedName("staffDetails") val staffDetails: String = ""
)

data class BasePay(
    @SerializedName("level") val level: String = "",
    @SerializedName("cost") val cost: String = "",
    @SerializedName("_id") val _id: String = ""
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
    @SerializedName("minRange") val minRange: String = "",
    @SerializedName("maxRange") val maxRange: String = ""
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
    val paymentOption: String = "",
    val payment: String = "",
    val division: String = "",
    val sendPushNotification: Boolean = false,
    val termsAndCondition: Boolean = false,
    val privacy: Boolean = false,
)

data class GameStaffRegisterRequest(
    val eventId: String = "",
    val role: String = "",
    val location: Location = Location()
)

data class Invites(
    @SerializedName("status") val status: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("profileImage") val profileImage: String = "",
    @SerializedName("reason") val reason: String = "",
    @SerializedName("_id") val Id: String = ""
)

data class CoachId(
    @SerializedName("_id") val Id: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("profileImage") val profileImage: String = ""
)

data class ScheduleResponse(
    @SerializedName("_id") val _id: String = "",
    @SerializedName("totalgames") val totalgames: String = "",
    @SerializedName("eventid") val eventid: String = "",
    @SerializedName("divisionid") val divisionid: String = "",
    @SerializedName("event") val event: EventDetail = EventDetail(),
    @SerializedName("matches") val matches: List<Matches> = mutableListOf(),
)

data class EventDetail(
    @SerializedName("_id") val _id: String = "",
    @SerializedName("address") val address: String = "",
    @SerializedName("city") val city: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("state") val state: String = "",
    @SerializedName("zip") val zip: String = "",
    @SerializedName("locationDesc") val locationDesc: String = "",
    @SerializedName("centralLocation") val centralLocation: String = "",
    @SerializedName("logo") val logo: String = "",
    @SerializedName("startDate") val startDate: String = "",
    @SerializedName("location") val location: Location = Location(),
)

data class Matches(
    @SerializedName("pairs") val pairs: List<ScheduleTeam> = mutableListOf(),
    @SerializedName("timeSlot") val timeSlot: String = "",
)

data class ScheduleTeam(@SerializedName("teams") val teams: List<Pairs> = mutableListOf())

data class Pairs(
    @SerializedName("_id") val _id: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("logo") val logo: String = "",
    @SerializedName("scheduleId") val scheduleId: String = ""
)

data class PublishedGames(
    @SerializedName("_id") val Id: String = "",
    @SerializedName("pairname") val pairname: String = "",
    @SerializedName("date") val date: String = "",
    @SerializedName("timeslot") val timeslot: String = "",
    @SerializedName("teams") val teams: ArrayList<Team> = arrayListOf(),
    @SerializedName("gameData") val gameData: GameData = GameData()

)

data class GameData(
    @SerializedName("_id") val Id: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("logo") val logo: String = ""
)
package com.allballapp.android.data.response.game

import com.allballapp.android.data.response.team.Team
import com.allballapp.android.ui.features.home.events.Invites
import com.allballapp.android.ui.features.venue.Location
import com.allballapp.android.ui.features.venue.VenueAddress
import com.google.gson.annotations.SerializedName

data class GameDetails(
    @SerializedName("_id") val Id: String = "",
    @SerializedName("eventid") val eventid: String = "",
    @SerializedName("pairname") val pairname: String = "",
    @SerializedName("teams") val teams: ArrayList<Team> = arrayListOf(),
    @SerializedName("divisionid") val divisionid: String = "",
    @SerializedName("date") val date: String = "",
    @SerializedName("timeslot") val timeslot: String = "",
    @SerializedName("venueid") val venueid: Venueid = Venueid(),
    @SerializedName("courtid") val courtid: CourtId = CourtId(),
    @SerializedName("staffRequirementdetail") val staffRequirementdetail: String = "",
    @SerializedName("status") val status: String = "",
    @SerializedName("createdAt") val createdAt: String = "",
    @SerializedName("updatedAt") val updatedAt: String = "",
    @SerializedName("invitations") val invitations: ArrayList<Invites> = arrayListOf(),
    @SerializedName("currentDate") val currentDate: String = ""
)

data class Venueid(

    @SerializedName("venueName") var venueName: String = "",
    @SerializedName("venueAddress") var venueAddress: VenueAddress = VenueAddress(),
    @SerializedName("venueLocation") var venueLocation: Location = Location()

)

data class CourtId(

    @SerializedName("courtName") var courtName: String = "",
    @SerializedName("exteriorPhotos") var exteriorPhotos: ArrayList<String> = arrayListOf(),
    @SerializedName("courtFloorPhotos") var courtFloorPhotos: ArrayList<String> = arrayListOf(),
    @SerializedName("gymPhotos") var gymPhotos: ArrayList<String> = arrayListOf()

)
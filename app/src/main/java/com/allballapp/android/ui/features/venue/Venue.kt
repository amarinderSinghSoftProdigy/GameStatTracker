package com.allballapp.android.ui.features.venue

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

data class Venue(
    @SerializedName("_id") val Id: String = "",
    @SerializedName("venueName") val venueName: String? = null,
    @SerializedName("createdAt") val createdAt: String = "",
    @SerializedName("courtInfo") val courtInfo: List<CourtInfo> = listOf(),
    @SerializedName("totalCourts") val totalCourts: Int = 0,
    @SerializedName("location") val location: String? = null,
    @SerializedName("distance") val distance: String = "",
    @SerializedName("ratings") val ratings: List<Int> = listOf(),
    @SerializedName("courtCost") val courtCost: String = "",
    @SerializedName("availabilityDays") val availabilityDays: String = ""
)


data class CourtInfo(
    @SerializedName("_id") val Id: String = "",
    @SerializedName("courtName") val courtName: String = "",
    @SerializedName("type") val type: String = "",
    @SerializedName("courtRating") val courtRating: String = "",
    @SerializedName("numberOfGyms") val numberOfGyms: String = "",
    @SerializedName("costPerHour") val costPerHour: String = "",
    @SerializedName("seatingRows") val seatingRows: String = "",
    @SerializedName("floorRating") val floorRating: String = "",
    @SerializedName("floorMaterial") val floorMaterial: String = "",
    @SerializedName("baselineDistance") val baselineDistance: String = "",
    @SerializedName("sidelineDistance") val sidelineDistance: String = "",
    @SerializedName("exteriorPhotos") val exteriorPhotos: List<String> = listOf(),
    @SerializedName("courtFloorPhotos") val courtFloorPhotos: List<String> = listOf(),
    @SerializedName("gymPhotos") val gymPhotos: List<String> = listOf(),
    @SerializedName("locationAndParkingInstructions") val locationAndParkingInstructions: String = "",
    @SerializedName("courtCost") val courtCost: String = "",
    @SerializedName("availabilityDays") val availabilityDays: String = "",
    @SerializedName("isDelete") val isDelete: Boolean = false,
    @SerializedName("createdBy") val createdBy: String = "",
    @SerializedName("createdAt") val createdAt: String = "",
    @SerializedName("updatedAt") val updatedAt: String = "",
    @SerializedName("__v") val _v: String = ""

)

data class VenueDetails(
    @SerializedName("_id") val Id: String = "",
    @SerializedName("venueName") val venueName: String = "",
    @SerializedName("courtId") val courtId: ArrayList<CourtInfo> = arrayListOf(),
    @SerializedName("venueAddress") val venueAddress: VenueAddress = VenueAddress(),
    @SerializedName("location") val location: String? = null,
    @SerializedName("venueLocation") val venueLocation: com.allballapp.android.data.request.Location = com.allballapp.android.data.request.Location(),
    @SerializedName("manager") val manager: Manager = Manager(),
    @SerializedName("scheduleManager") val scheduleManager: Manager = Manager(),
    @SerializedName("paymentManager") val paymentManager: Manager = Manager(),
    @SerializedName("facilityManager") val facilityManager: Manager = Manager()
)

data class Manager(
    @SerializedName("name") val name: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("phone") val phone: String = ""
)

data class VenueAddress(
    @SerializedName("address") val address: String = "",
    @SerializedName("state") val state: String = "",
    @SerializedName("city") val city: String = "",
    @SerializedName("zipCode") val zipCode: String = ""
)

data class Location(
    val address: String = "",
    val state: String = "",
    val city: String = "",
    val zipCode: String = "",
    var latLong: LatLng = LatLng(0.0, 0.0)
)
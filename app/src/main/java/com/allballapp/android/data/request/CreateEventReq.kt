package com.allballapp.android.data.request

import com.google.gson.annotations.SerializedName

data class CreateEventReq(
    @SerializedName("eventName") val eventName: String = "",
    @SerializedName("eventType") val eventType: String = "",
    @SerializedName("myTeam") val myTeam: String = "",
    @SerializedName("date") val date: String = "",
    @SerializedName("arrivalTime") val arrivalTime: String = "",
    @SerializedName("startTime") val startTime: String = "",
    @SerializedName("endTime") val endTime: String = "",
    @SerializedName("landmarkLocation") val landmarkLocation: String = "",
    @SerializedName("prePractice") val prePractice: String = "",
    @SerializedName("location") val location: Location = Location(),
    @SerializedName("address") val address: Address = Address(),
    @SerializedName("pushNotification") val pushNotification: Boolean = false
)

data class Location(
    @SerializedName("type") val type: String = "",
    @SerializedName("coordinates") val coordinates: ArrayList<Double> = arrayListOf()
)

data class Address(
    @SerializedName("address") var address: String = "",
    @SerializedName("street") var street: String = "",
    @SerializedName("state") var state: String = "",
    @SerializedName("city") var city: String = "",
    @SerializedName("country") var country: String = "",
    @SerializedName("zip") var zip: String = "",
    @SerializedName("lat") var lat: Double = 0.00,
    @SerializedName("long") var long: Double = 0.00,
)
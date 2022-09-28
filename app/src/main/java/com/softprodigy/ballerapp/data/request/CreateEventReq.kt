package com.softprodigy.ballerapp.data.request

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
    @SerializedName("street") val street: String = "",
    @SerializedName("state") val state: String = "",
    @SerializedName("city") val city: String = "",
    @SerializedName("zip") val zip: String? = ""
)
package com.allballapp.android.data.response

import com.google.gson.annotations.SerializedName

data class MyLeagueResponse(
    @SerializedName("__v")
    val __v: Int,
    @SerializedName("_id")
    val _id: String,
    @SerializedName("event")
    val event: String,
    @SerializedName("eventDetail")
    val eventDetail: EventDetail,
    @SerializedName("payment")
    val payment: String,
    @SerializedName("paymentOption")
    val paymentOption: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("team")
    val team: String
)

data class EventDetail(
    @SerializedName("_id")
    val _id: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("eventType")
    val eventType: String,
    @SerializedName("logo")
    val logo: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("zip")
    val zip: String,
)
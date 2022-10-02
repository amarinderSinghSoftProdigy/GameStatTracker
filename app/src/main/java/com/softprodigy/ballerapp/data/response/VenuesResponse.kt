package com.softprodigy.ballerapp.data.response

import com.google.gson.annotations.SerializedName

data class VenuesResponse(
    @SerializedName("__v")
    val __v: Int,
    @SerializedName("_id")
    val _id: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdBy")
    val createdBy: String,
    @SerializedName("division")
    val division: String,
    @SerializedName("event")
    val event: Event,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("isDeleted")
    val isDeleted: Boolean,
    @SerializedName("paid")
    val paid: Boolean,
    @SerializedName("payment")
    val payment: String,
    @SerializedName("paymentOption")
    val paymentOption: String,
    @SerializedName("players")
    val players: List<String>,
    @SerializedName("privacy")
    val privacy: Boolean,
    @SerializedName("sendPushNotification")
    val sendPushNotification: Boolean,
    @SerializedName("status")
    val status: String,
    @SerializedName("team")
    val team: String,
    @SerializedName("termsAndCondition")
    val termsAndCondition: Boolean,
    @SerializedName("updatedAt")
    val updatedAt: String
)

data class Event(
    @SerializedName("_id")
    val _id: String,
    @SerializedName("venuesId")
    val venuesId: List<VenuesId>
)

data class VenuesId(
    @SerializedName("_id")
    val _id: String,
    @SerializedName("venueName")
    val venueName: String,
    @SerializedName("logo")
    val logo: String
)
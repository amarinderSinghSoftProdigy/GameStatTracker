package com.allballapp.android.data.response

import com.allballapp.android.ui.features.home.events.Participation
import com.google.gson.annotations.SerializedName

data class MyLeagueResponse(
    @SerializedName("_id")
    val _id: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("endDate")
    val endDate: String,
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
    @SerializedName("participation")
    val participation: Participation = Participation(),
    @SerializedName("division")
    val divisionDetail: DivisionDetail = DivisionDetail()
)

data class DivisionDetail(
    @SerializedName("_id")
    val _id: String = "",
    @SerializedName("divisionName")
    val divisionName: String = "",
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
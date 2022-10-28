package com.allballapp.android.data.response.homepage

import com.google.gson.annotations.SerializedName

data class HomePageCoachModel(
    @SerializedName("myEvents") val myEvents: Int = 0,
    @SerializedName("opportunityToPlay") val opportunityToPlay: Int = 0,
    @SerializedName("opportunityToWork") val opportunityToWork: Int = 0,
    @SerializedName("myLeagues") val myLeagues: Int = 0,
    @SerializedName("allLeagues") val allLeagues: Int = 0,
    @SerializedName("pendingInvitations") val pendingInvitations: Int = 0
)
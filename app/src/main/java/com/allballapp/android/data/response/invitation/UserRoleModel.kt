package com.allballapp.android.data.response.invitation

import com.google.gson.annotations.SerializedName

data class UserRoleModel(
    @SerializedName("key") val key: String = "",
    @SerializedName("value") val value: String = ""
)
data class InvitationData(
    @SerializedName("failedMobileNumbers") val failedMobileNumber : ArrayList<String> = arrayListOf(),
    @SerializedName("failedProfiles") val failedProfiles : ArrayList<String> = arrayListOf()
)
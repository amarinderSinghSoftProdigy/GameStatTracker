package com.softprodigy.ballerapp.data.response

import com.google.gson.annotations.SerializedName

data class PlayerDetails(
    @SerializedName("_id") val id: String = "",
    @SerializedName("memberId") val memberDetails: MemberDetails = MemberDetails(),
    var checked: Boolean = false,
    @SerializedName("role") val role: String = ""
)

data class MemberDetails(
    @SerializedName("_id") val id: String = "",
    @SerializedName("firstName") val firstName: String = "",
    @SerializedName("lastName") val lastName: String = "",
    @SerializedName("profileImage") val profileImage: String = "",
    @SerializedName("gender") val gender: String = "",
    @SerializedName("name") val name: String = ""
)
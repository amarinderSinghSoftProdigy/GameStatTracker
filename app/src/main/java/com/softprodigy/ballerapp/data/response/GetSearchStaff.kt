package com.softprodigy.ballerapp.data.response

import com.google.gson.annotations.SerializedName

data class GetSearchStaff(
    @SerializedName("_id") val _id: String = "",
    @SerializedName("profileImage") val profileImage: String = "",
    @SerializedName("name") val name: String = ""
)
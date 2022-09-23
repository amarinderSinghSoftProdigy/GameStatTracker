package com.softprodigy.ballerapp.data.response

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id") val _Id: String = "",
    @SerializedName("firstName") val firstName: String = "",
    @SerializedName("lastName") val lastName: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("role") val role: String = "",
    @SerializedName("phoneVerified") val phoneVerified: Boolean = false,
    @SerializedName("name") val name: String = "",
    @SerializedName("address") val address: String = "",
    @SerializedName("gender") val gender: String = "",
    @SerializedName("birthdate") val birthdate: String = "",
    @SerializedName("id") val id: String = "",
    @SerializedName("profileImage") val profileImage: String = ""
)

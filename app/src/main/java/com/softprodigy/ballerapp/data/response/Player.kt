package com.softprodigy.ballerapp.data.response

import com.google.gson.annotations.SerializedName

data class Player(
    @SerializedName("_id") val Id: String? = null,
    @SerializedName("firstName") val firstName: String? = null,
    @SerializedName("lastName") val lastName: String? = null,
    @SerializedName("profileImage") val profileImage: String? = null,
    @SerializedName("email") val email: String = "",
    @SerializedName("role") val role: String? = null,
    @SerializedName("birthdate") val birthdate: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("name") val name: String
)
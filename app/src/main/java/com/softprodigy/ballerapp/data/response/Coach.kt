package com.softprodigy.ballerapp.data.response

import com.google.gson.annotations.SerializedName

data class Coach(
    @SerializedName("_id") val Id: String = "",
    @SerializedName("firstName") val firstName: String = "",
    @SerializedName("lastName") val lastName: String = "",
    @SerializedName("profileImage") val profileImage: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("role") val role: String = "",
    @SerializedName("birthdate") val birthdate: String = "",
    @SerializedName("address") val address: String = "",
    @SerializedName("refreshToken") val refreshToken: String = "",
    @SerializedName("refreshTokenExpireAt") val refreshTokenExpireAt: String = "",
    @SerializedName("phone") val phone: String = "",
    @SerializedName("phoneVerified") val phoneVerified: Boolean = false,
    @SerializedName("isDelete") val isDelete: Boolean = false,
    @SerializedName("gender") val gender: String = "",
    @SerializedName("createdAt") val createdAt: String = "",
    @SerializedName("updatedAt") val updatedAt: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("__v") val _v: Int? = null,
    @SerializedName("loginType") val loginType: String = ""
)

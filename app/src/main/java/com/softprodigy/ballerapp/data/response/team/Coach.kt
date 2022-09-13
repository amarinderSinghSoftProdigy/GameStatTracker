package com.softprodigy.ballerapp.data.response.team

import com.google.gson.annotations.SerializedName

data class Coach(
    @SerializedName("__v")
    val __v: Int? = null,
    @SerializedName("_id")
    val _id: String? = null,
    @SerializedName("address")
    val address: String? = "",
    @SerializedName("assists")
    val assists: String? = "",
    @SerializedName("awardPoints")
    val awardPoints: String? = "",
    @SerializedName("birthdate")
    val birthdate: String? = "",
    @SerializedName("coachId")
    val coachId: String? = "",
    @SerializedName("coachPosition")
    val coachPosition: String? = "",
    @SerializedName("createdAt")
    val createdAt: String? = "",
    @SerializedName("email")
    val email: String? = "",
    @SerializedName("fgPerc")
    val fgPerc: String? = "",
    @SerializedName("firstName")
    val firstName: String? = "",
    @SerializedName("gender")
    val gender: String? = "",
    @SerializedName("isDelete")
    val isDelete: Boolean = false,
    @SerializedName("lastName")
    val lastName: String? = "",
    @SerializedName("loginType")
    val loginType: String? = "",
    @SerializedName("name")
    val name: String? = "",
    @SerializedName("password")
    val password: String? = "",
    @SerializedName("phone")
    val phone: String? = "",
    @SerializedName("phoneVerified")
    val phoneVerified: Boolean = false,
    @SerializedName("practiceThereeS")
    val practiceThereeS: String? = "",
    @SerializedName("profileImage")
    val profileImage: String? = "",
    @SerializedName("refreshToken")
    val refreshToken: String? = "",
    @SerializedName("refreshTokenExpireAt")
    val refreshTokenExpireAt: String? = "",
    @SerializedName("reounding")
    val reounding: String? = "",
    @SerializedName("role")
    val role: String? = "",
    @SerializedName("steals")
    val steals: String? = "",
    @SerializedName("teamId")
    val teamId: String? = "",
    @SerializedName("threePtPerc")
    val threePtPer: String? = "",
    @SerializedName("totalFtS")
    val totalFtS: String? = "",
    @SerializedName("totalGamePoints")
    val totalGamePoints: String? = "",
    @SerializedName("totalGameRebounds")
    val totalGameRebounds: String? = "",
    @SerializedName("totalThreeS")
    val totalThreeS: String? = "",
    @SerializedName("updatedAt")
    val updatedAt: String? = "",
    @SerializedName("userId")
    val userId: String? = ""
)
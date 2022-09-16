package com.softprodigy.ballerapp.data.response.roaster

import com.google.gson.annotations.SerializedName

data class RoasterResponse(
    @SerializedName("_id") val id: String = "",
    @SerializedName("colorCode") val colorCode: String = "",
    @SerializedName("logo") val logo: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("playerDetails") val playerDetails: List<PlayerDetail> = arrayListOf(),
    @SerializedName("coachDetails") val coachDetails: List<PlayerDetail> = arrayListOf()
)

data class PlayerDetail(
    @SerializedName("__v")
    val __v: Int? = null,
    @SerializedName("_id")
    val _id: String = "",
    @SerializedName("accessPermissions")
    val accessPermissions: List<Any> = arrayListOf(),
    @SerializedName("address")
    val address: String = "",
    @SerializedName("birthdate")
    val birthdate: String = "",
    @SerializedName("city")
    val city: String = "",
    @SerializedName("classOf")
    val classOf: String = "",
    @SerializedName("createdAt")
    val createdAt: String = "",
    @SerializedName("documents")
    val documents: Documents? = null,
    @SerializedName("email")
    val email: String = "",
    @SerializedName("favActivePlayer")
    val favActivePlayer: String = "",
    @SerializedName("favAllTimePlayer")
    val favAllTimePlayer: String = "",
    @SerializedName("favCollegeTeam")
    val favCollegeTeam: String = "",
    @SerializedName("favNbaTeam")
    val favNbaTeam: String = "",
    @SerializedName("firstName")
    val firstName: String = "",
    @SerializedName("gender")
    val gender: String = "",
    @SerializedName("grade")
    val grade: String = "",
    @SerializedName("isDelete")
    val isDelete: Boolean = false,
    @SerializedName("jerseyNumber")
    val jerseyNumber: String = "",
    @SerializedName("jerseyNumberPerferences")
    val jerseyNumberPerferences: List<String> = arrayListOf(),
    @SerializedName("lastName")
    val lastName: String = "",
    @SerializedName("loginType")
    val loginType: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("password")
    val password: String = "",
    @SerializedName("phone")
    val phone: String = "",
    @SerializedName("phoneVerified")
    val phoneVerified: Boolean = false,
    @SerializedName("playerId")
    val playerId: String = "",
    @SerializedName("position")
    val position: String = "",
    @SerializedName("positionPlayed")
    val positionPlayed: List<String> = arrayListOf(),
    @SerializedName("profileImage")
    val profileImage: String = "",
    @SerializedName("refreshToken")
    val refreshToken: String = "",
    @SerializedName("refreshTokenExpireAt")
    val refreshTokenExpireAt: String = "",
    @SerializedName("role")
    val role: String = "",
    @SerializedName("shirtSize")
    val shirtSize: String = "",
    @SerializedName("state")
    val state: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("teamId")
    val teamId: List<String> = arrayListOf(),
    @SerializedName("updatedAt")
    val updatedAt: String = "",
    @SerializedName("waistSize")
    val waistSize: String = "",
    @SerializedName("zip")
    val zip: String = ""
)

data class Documents(
    @SerializedName("auuCard")
    val auuCard: String = "",
    @SerializedName("birthCertificate")
    val birthCertificate: String = "",
    @SerializedName("gradeVerfication")
    val gradeVerification: String = "",
    @SerializedName("permissionSlip")
    val permissionSlip: String = "",
    @SerializedName("vaccineCard")
    val vaccineCard: String = "",
    @SerializedName("waiver")
    val waiver: String = ""
)
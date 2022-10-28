package com.allballapp.android.data.response.team

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class Player(
    var uniqueId: Int = 0,
    @SerializedName("__v")
    val __v: Int? = null,
    @SerializedName("_id")
    val _id: String = "",
    @SerializedName("accessPermissions")
    val accessPermissions: List<Any> = emptyList(),
    @SerializedName("address")
    val address: String? = "",
    @SerializedName("assists")
    val assists: String? = "",
    @SerializedName("awardPoints")
    val awardPoints: String? = "",
    @SerializedName("birthdate")
    val birthdate: String? = "",
    @SerializedName("city")
    val city: String? = "",
    @SerializedName("classOf")
    val classOf: String? = "",
    @SerializedName("createdAt")
    val createdAt: String? = "",
    @SerializedName("documents")
    val documents: Documents? = null,
    @SerializedName("email")
    val email: String? = "",
    @SerializedName("favActivePlayer")
    val favActivePlayer: String? = "",
    @SerializedName("favAllTimePlayer")
    val favAllTimePlayer: String? = "",
    @SerializedName("favCollegeTeam")
    val favCollegeTeam: String? = "",
    @SerializedName("favNbaTeam")
    val favNbaTeam: String? = "",
    @SerializedName("fgPerc")
    val fgPerc: String? = "",
    @SerializedName("firstName")
    val firstName: String? = "",
    @SerializedName("gender")
    val gender: String? = "",
    @SerializedName("grade")
    val grade: String? = "",
    @SerializedName("isDelete")
    val isDelete: Boolean = false,
    @SerializedName("jerseyNumber")
    val jerseyNumber: String = "",
    @SerializedName("jerseyNumberPerferences")
    val jerseyNumberPreferences: List<String> = emptyList(),
    @SerializedName("lastName")
    val lastName: String? = "",
    @SerializedName("loginType")
    val loginType: String? = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("password")
    val password: String? = "",
    @SerializedName("phone")
    val phone: String? = "",
    @SerializedName("phoneVerified")
    val phoneVerified: Boolean = false,
    @SerializedName("playerId")
    val playerId: String? = "",
    @SerializedName("position")
    var position: String = "",
    @SerializedName("positionPlayed")
    val positionPlayed: List<String> = emptyList(),
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
    @SerializedName("resetPasswordExpire")
    val resetPasswordExpire: String? = "",
    @SerializedName("resetPasswordToken")
    val resetPasswordToken: String? = "",
    @SerializedName("role")
    val role: String? = "",
    @SerializedName("shirtSize")
    val shirtSize: String? = "",
    @SerializedName("state")
    val state: String? = "",
    @SerializedName("status")
    val status: String? = "",
    @SerializedName("steals")
    val steals: String? = "",
    @SerializedName("teamId")
    val teamId: String = "",
    @SerializedName("threePtPerc")
    val threePtPerc: String? = "",
    @SerializedName("totalFtS")
    val totalFtS: String? = "",
    @SerializedName("totalGamePoints")
    val totalGamePoints: String? = "",
    @SerializedName("totalGameRebounds")
    val totalGameRebounds: String? = "",
    @SerializedName("total3s")
    val total3s: String? = "",
    @SerializedName("updatedAt")
    val updatedAt: String? = "",
    @SerializedName("userId")
    val userId: String? = "",
    @SerializedName("waistSize")
    val waistSize: String? = "",
    @SerializedName("zip")
    val zip: String? = "",
    @SerializedName("fieldGoal")
    val fieldGoal: String? = "",
    @SerializedName("practice3s")
    val practice3s: String? = "",
    @SerializedName("thirdPoint")
    val thirdPoint: String? = "",
    @SerializedName("userInviteData")
    val userInviteData: UserInviteData? = UserInviteData(),

    var locked: Boolean = false

) {
    override fun toString(): String {
        var gson = Gson();
        var json = gson.toJson(this, Player::class.java);
        return json;
    }

}
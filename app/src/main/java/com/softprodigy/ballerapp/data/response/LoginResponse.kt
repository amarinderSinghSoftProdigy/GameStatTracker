package com.softprodigy.ballerapp.data.response

import com.google.gson.annotations.SerializedName
import com.softprodigy.ballerapp.data.UserInfo

/*data class LoginResponse(
//     @SerializedName("data") var userInfo: UserInfo = UserInfo(),
//     @SerializedName("status") var status: Int? = null,
//     @SerializedName("message") var message: String? = null,
//     @SerializedName("verifyToken") var verifyToken: String? = null
//    @SerializedName("status") val status: Boolean,
//    @SerializedName("statusCode") val statusCode: Int? = null,
//    @SerializedName("statusMessage") val statusMessage: String? = null,
//    @SerializedName("totalRecords") val totalRecords: Int? = null,
    @SerializedName("data") val userInfo: UserInfo? = null
)*/

data class UserInfo(
    @SerializedName("token") var token: String? = null,
    @SerializedName("tokenExpireAt") var tokenExpireAt: Int? = null,
    @SerializedName("refreshToken") var refreshToken: String? = null,
    @SerializedName("refreshTokenExpireAt") var refreshTokenExpireAt: Int? = null

)




package com.softprodigy.ballerapp.data.response

import com.google.gson.annotations.SerializedName
data class UserInfo(
    @SerializedName("token") val token: String = "",
    @SerializedName("tokenExpireAt") val tokenExpireAt: Int? = null,
    @SerializedName("refreshToken") val refreshToken: String? = null,
    @SerializedName("refreshTokenExpireAt") val refreshTokenExpireAt: Int? = null

)




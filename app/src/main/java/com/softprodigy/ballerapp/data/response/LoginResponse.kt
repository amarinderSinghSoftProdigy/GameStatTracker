package com.softprodigy.ballerapp.data.response

import com.google.gson.annotations.SerializedName
data class UserInfo(
    @SerializedName("token") var token: String? = null,
    @SerializedName("tokenExpireAt") var tokenExpireAt: Int? = null,
    @SerializedName("refreshToken") var refreshToken: String? = null,
    @SerializedName("refreshTokenExpireAt") var refreshTokenExpireAt: Int? = null

)




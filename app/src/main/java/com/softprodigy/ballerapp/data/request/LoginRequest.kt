package com.softprodigy.ballerapp.data.request

import com.google.gson.annotations.SerializedName


data class LoginRequest(
    @SerializedName("email") val email: String? = null,
    @SerializedName("password") val password: String? = null,
    @SerializedName("loginType") val loginType: String? = null,
    @SerializedName("facebookId") val facebookId: String? = null,
    @SerializedName("twitterId") val twitterId: String? = null,
    @SerializedName("googleId") val googleId: String? = null,
    @SerializedName("firstName") val firstName: String? = null,
    @SerializedName("lastName") val lastName: String? = null
)
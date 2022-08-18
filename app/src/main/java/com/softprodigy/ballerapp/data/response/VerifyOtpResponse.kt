package com.softprodigy.ballerapp.data.response

import com.google.gson.annotations.SerializedName

data class VerifyOtpResponse(
    @SerializedName("data")
    val userInfo: User = User(),
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
)

data class User(
    @SerializedName("first_name") var firstName: String? = null,
    @SerializedName("last_name") var lastName: String? = null,
    @SerializedName("mobile") var mobile: String? = null,
    @SerializedName("email") var email: String = "",
)

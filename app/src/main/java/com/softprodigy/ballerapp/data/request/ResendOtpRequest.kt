package com.softprodigy.ballerapp.data.request

import com.google.gson.annotations.SerializedName

data class ResendOtpRequest(
    @SerializedName("email") val email: String,
    @SerializedName("mobile") val mobile: String,
    @SerializedName("otpType") val otpType: String
)
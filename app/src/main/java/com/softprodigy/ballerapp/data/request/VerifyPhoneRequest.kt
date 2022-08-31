package com.softprodigy.ballerapp.data.request

import com.google.gson.annotations.SerializedName

data class VerifyPhoneRequest(@SerializedName("phone") val phone: String? = null)

data class ConfirmPhoneRequest(
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("otp") val otp: String? = null
)
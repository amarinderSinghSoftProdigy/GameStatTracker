package com.softprodigy.ballerapp.data.request

import com.google.gson.annotations.SerializedName


data class LoginRequest(
    @SerializedName("email") var email: String? = null,
    @SerializedName("password") var password: String? = null,
    @SerializedName("device_type") var deviceType: String? = null,
    @SerializedName("device_token") var deviceToken: String? = null
)
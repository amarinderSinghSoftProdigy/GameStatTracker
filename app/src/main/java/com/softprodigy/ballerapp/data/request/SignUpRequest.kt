package com.softprodigy.ballerapp.data.request

import com.google.gson.annotations.SerializedName


data class SignUpRequest(
    @SerializedName("first_name") var firstName: String? = null,
    @SerializedName("last_name") var lastName: String? = null,
    @SerializedName("mobile") var mobile: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("password") var password: String? = null,
    @SerializedName("device_type") var deviceType: String? = null,
    @SerializedName("device_token") var deviceToken: String? = null
)
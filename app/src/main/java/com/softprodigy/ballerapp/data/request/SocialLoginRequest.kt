package com.softprodigy.ballerapp.data.request

import com.google.gson.annotations.SerializedName

data class SocialLoginRequest(
    @SerializedName("first_name") var firstName: String? = null,
    @SerializedName("last_name") var lastName: String? = null,
    @SerializedName("device_type") var deviceType: String? = null,
    @SerializedName("provider_type") var provider_type: String? = null,
    @SerializedName("device_token") var deviceToken: String? = null,
    @SerializedName("provider_id") var providerId: String? = null,
    @SerializedName("provider_token") var providerToken: String? = null,
    @SerializedName("email") var email: String? = null
)

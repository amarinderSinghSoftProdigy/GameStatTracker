package com.softprodigy.deliveryapp.data

import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("first_name") var firstName: String? = null,
    @SerializedName("last_name") var lastName: String? = null,
    @SerializedName("mobile") var mobile: String? = null,
    @SerializedName("isEmailVerified") var isEmailVerified: Boolean = false,
    @SerializedName("isMobileVerified") var isMobileVerified: Boolean = false,
    @SerializedName("email") var email: String = "",
    @SerializedName("token") var token: String = ""
)
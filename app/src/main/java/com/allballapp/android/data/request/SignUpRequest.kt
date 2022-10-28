package com.allballapp.android.data.request

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    @SerializedName("firstName") val firstName: String? = null,
    @SerializedName("lastName") val lastName: String? = null,
    @SerializedName("profileImage") val profileImage: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("phoneVerified") val phoneVerified: Boolean? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("birthdate") val birthdate: String? = null,
    @SerializedName("role") val role: String? = null,
    @SerializedName("password") val password: String? = null,
    @SerializedName("repeat_password") val repeatPassword: String? = null
)
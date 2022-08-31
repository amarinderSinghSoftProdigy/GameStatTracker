package com.softprodigy.ballerapp.data.request

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignUpData(
    @SerializedName("firstName") val firstName: String? = null,
    @SerializedName("lastName") val lastName: String? = null,
    @SerializedName("profileImage") val profileImage: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("phoneVerified") val phoneVerified: Boolean? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("birthdate") val birthdate: String? = null,
    @SerializedName("role") val role: String? = null,
    @SerializedName("password") val password: String? = null,
    @SerializedName("repeat_password") val repeatPassword: String? = null,
) : Parcelable


class SignUpType : NavType<SignUpData>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): SignUpData? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): SignUpData {
        return Gson().fromJson(value, SignUpData::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: SignUpData) {
        bundle.putParcelable(key, value)
    }
}
package com.softprodigy.ballerapp.data.request

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignUpData(
    @SerializedName("firstName") val firstName: String = "",
    @SerializedName("lastName") val lastName: String = "",
    @SerializedName("profileImage") val profileImage: String? = null,//serverImage
    @SerializedName("profileImageUri") val profileImageUri: String? = null,//localUriImage
    @SerializedName("email") var email: String? = null,
    @SerializedName("phone") val phone: String = "",
    @SerializedName("address") val address: String = "",
    @SerializedName("phoneVerified") val phoneVerified: Boolean = false,
    @SerializedName("gender") val gender: String = "",
    @SerializedName("birthdate") val birthdate: String = "",
    @SerializedName("role") val role: String? = null,
    @SerializedName("password") val password: String? = null,
    @SerializedName("repeat_password") val repeatPassword: String? = null,
    @SerializedName("token") val token: String? = null,
    @SerializedName("id") val id: String? = null,
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
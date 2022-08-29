package com.softprodigy.ballerapp.ui.features.sign_up

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import java.sql.Struct

@Parcelize
data class SignUpData(
    val firstName: String? = null,
    val lastName: String? = null,
    val profileImage: String? = null,
    var email: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val phoneVerified: Boolean? = null,
    val gender: String? = null,
    val birthdate: String? = null,
    val role: String? = null,
    val password: String? = null,
    val repeatPassword: String? = null
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